package com.fdt.security.service.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fdt.common.dto.PageRecordsDTO;
import com.fdt.common.exception.SDLBusinessException;
import com.fdt.ecom.dao.EComDAO;
import com.fdt.ecom.entity.NodeConfiguration;
import com.fdt.email.EmailProducer;
import com.fdt.recurtx.dao.RecurTxDAO;
import com.fdt.recurtx.dto.OverriddenSubscriptionDTO;
import com.fdt.security.dao.UserAdminDAO;
import com.fdt.security.dao.UserDAO;
import com.fdt.security.dto.FirmUserDTO;
import com.fdt.security.dto.SearchCriteriaDTO;
import com.fdt.security.entity.Access;
import com.fdt.security.entity.User;
import com.fdt.security.exception.UserNameNotFoundException;
import com.fdt.subscriptions.dao.SubDAO;
import com.fdt.subscriptions.dto.SubscriptionDTO;

@Service("userAdminService")
public class UserAdminServiceImpl implements UserAdminService {

    private static final Logger logger = LoggerFactory.getLogger(UserAdminServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserAdminDAO userAdminDAO;

    @Autowired
    private SubDAO subDAO;

    @Autowired
    private RecurTxDAO recurTxDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailProducer emailProducer;

    @Autowired
    private EComDAO eComDAO = null;

    @Value("${ecommerce.serverurl}")
    private String ecomServerURL = null;

    @Autowired
    @Qualifier("serverMessageSource")
    private MessageSource messages = null;

    /** This Method Is Called From EcomAdmin To Get The Details Of The User.
     * @param userName E-Mail Id Of User.
     * @return User.
     * @throws UserNameNotFoundException
     */
    @Transactional(readOnly = true)
    public User getUserDetailsForAdmin(String userName)  throws UserNameNotFoundException {
        Assert.hasLength(userName, "userName Cannot be Null/Empty");
        User user = this.userDAO.getUserDetailsForAdmin(userName);
        if (user == null) {
            throw new UserNameNotFoundException(this.getMessage("security.username.notfound",
                new String[]{userName}));
        }
        return user;
    }

    /** This Method enables/disables userAccess.
     * @param userAccessId UserAccess Id.
     * @param isEnable Flag Which Determines Whether A UserAccess Should Be Enabled Or Disabled.
     * @param modifiedBy User Who Tried To Enable or Disable User Access.
     * @param comments Comments Supplied By The Admin User When He Tried To Enable/Disable User Access.
     * @param isAccessOverridden Flag to Indicate Whether Access Should Be Overridden.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor= Throwable.class)
    public void enableDisableUserAccess(Long userAccessId, boolean isEnable, String modifiedBy, String comments,
            boolean isAccessOverridden, String endDate) {
        Assert.notNull(userAccessId, "User Access id Cannot Be Null");
        Assert.hasLength(modifiedBy, "modifiedBy Cannot be Null/Empty");
        Assert.hasLength(comments,   "comments Cannot be Null/Empty");

        // Retrieve the user by user access id
        FirmUserDTO user = this.userDAO.getUserByUserAccessId(userAccessId);
        // Check if access is firm level access, if so then set the is_firm_access_admin flag to 'Y'
        int noOfRecordUpdated = 0;
        if(!user.isFirmLevelAccess()){
        	noOfRecordUpdated = this.userDAO.enableDisableUserAccess(userAccessId, isEnable, modifiedBy, comments,
                    isAccessOverridden, endDate);
	        if (noOfRecordUpdated == 0) {
	            logger.error("The User Access is not Disabled");
	            throw new RuntimeException("The User Access is not Disabled");
	        }
        }
    }

    /** This Method First Checks Whether The Username Is Archivable. A Particular User Is Archivable, If He Does not Have
     * Web Transactions, Recurring Transactions, UserAlerts And Shopping Cart Items.
     * @param userName E-Mail Id Of The User To Be Archived.
     * @param comments Comments Entered By The Administrator While The User Is Being Archived.
     * @param modifiedBy Administrator Who Tried To Archive The User.
     * @param machineName Machine From Which, Administrator Who Tried To Archive The User.
     * @throws SDLBusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor= Throwable.class)
    public void archiveUser(String userName, String comments, String modifiedBy, String machineName)
            throws SDLBusinessException {
        Assert.hasLength(userName,    "User Name Cannot Be Null/Empty");
        Assert.hasLength(comments,    "Comments Cannot Be Null/Empty");
        Assert.hasLength(modifiedBy,  "Modified By Cannot Be Null/Empty");
        Assert.hasLength(machineName, "Machine  Name Cannot Be Null/Empty");
        boolean isUserArchivable = this.userDAO.isUserArchivable(userName);
        if (isUserArchivable) {
            if (this.userDAO.archiveUser(userName, comments, modifiedBy, machineName) == 0) {
                logger.error("User " + userName + " cannot be archieved");
                throw new RuntimeException("User " + userName + " cannot be archieved");
            }
        } else {
            throw new SDLBusinessException("User Cannot Be Archived as He/She has transactions associated to them");
        }
    }

    /**The Method Will Check For Maximum Of Retention Days For All The Sites Which User Registered. This Method Will
     * Notify The User Once That Period Is Reached. (Meaning, For Those Many User Did Not Try To Log In.)
     * @return List Of Users
     */
    @Transactional(readOnly = true)
    public List<User> getInactiveUsers() {
        return this.userDAO.getInactiveUsers();
    }

    /** This Method Is called From Scheduler and Sends E-Mail To Each Inactive users.
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor= Throwable.class)
    public void notifyInactiveUsers(User user) {
	    NodeConfiguration nodeConfiguration = this.eComDAO.getNodeConfiguration(user.getRegisteredNode());
        Map<String, Object> emailData = new HashMap<String, Object>();
        emailData.put("firstName", user.getFirstName());
        emailData.put("lastName", user.getLastName());
        emailData.put("dateCreated", user.getCreatedDate());
        emailData.put("remainingDays", user.getCreatedDate());
        emailData.put("lastLoginDate", user.getLastLoginTime());
        emailData.put("accountDeletionDate", user.getAccountDeletionDate());
        emailData.put("user", user);
        this.emailProducer.sendMailUsingTemplate(nodeConfiguration.getFromEmailAddress(), user.getUsername(),
            nodeConfiguration.getInActiveUserNotifSubject(), nodeConfiguration.getEmailTemplateFolder() +
                nodeConfiguration.getInActiveUserNotifTemplate(), emailData);
        this.userDAO.updateisEmailNotificationSent(user.getUsername(), true);
    }

    /**
     * This method is called by the Scheduler. It will send a warning to a user that their overridden
     * subscription is about to expire. Then it will mark in the database that the user already received
     * their warning so that they don't get multiple ones.
     * 
     * @param overriddenSubscriptionDTO
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void warnExpiringOverriddenSubscription(OverriddenSubscriptionDTO overriddenSubscriptionDTO) {

        String fromEmailAddress = overriddenSubscriptionDTO.getFromEmailAddress();
        String templateFolder = overriddenSubscriptionDTO.getEmailTemplateFolder();
        String template = overriddenSubscriptionDTO.getOverriddenSubscriptionWarningTemplate();
        String subject = overriddenSubscriptionDTO.getOverriddenSubscriptionWarningSubject();
        String emailId = overriddenSubscriptionDTO.getEmailId();
        Date expireDate = overriddenSubscriptionDTO.getOverriddenUntillDate();
        Long accessId = overriddenSubscriptionDTO.getAccessId();

        User user = userDAO.getUser(emailId);
        List<SubscriptionDTO> subscriptions = subDAO.getUserSubs(emailId, null, null, true, false);

        Optional<SubscriptionDTO> subscription = subscriptions.stream().filter(s -> s.getAccessId().equals(accessId)).findFirst();
        String subscriptionSite = null;
        if (subscription.isPresent()) {
            subscriptionSite = subscription.get().getSiteName();
        } else {
            throw new RuntimeException("No subscription found matching the user's overridden subscription");
        }

        Map<String, Object> emailData = new HashMap<>();
        emailData.put("user", user);
        emailData.put("expireDate", expireDate);
        emailData.put("subscriptionSite", subscriptionSite);
        emailData.put("subscriptions", subscriptions);

        emailProducer.sendMailUsingTemplate(fromEmailAddress, emailId, subject, templateFolder
                + template, emailData);
        recurTxDAO.updateIsOverriddenSubscriptionWarningSent(overriddenSubscriptionDTO.getUserAccessId(), true);
    }

    private String getMessage(String messageKey, Object[] object) {
        return this.messages.getMessage(messageKey, object, new Locale("en"));
    }

    /**
     * This method returns users for a pagination.
     * Search Critieria has two properties(numberOfRecords, recordCount) that indicates the how many records to be returned
     * and which row to start from.
     *
     * @param searchCriteria
     * @return
     */
    @Transactional(readOnly = true)
    public PageRecordsDTO findUsers(SearchCriteriaDTO searchCriteria){
    	return this.userAdminDAO.findUsers(searchCriteria);
    }


	/**
	 *
	 * @param userName
	 * @param siteName
	 * @return
	 */
    @Transactional(readOnly = true)
    public List<SubscriptionDTO> getUserInfoForAdmin(String userName, String siteName){
    	return this.userAdminDAO.getUserInfoForAdmin(userName, siteName);
    }

    @Transactional(readOnly = true)
    public List<FirmUserDTO> getFirmUsersbySubscriptionAndUserName(String userName, Long accessId){
   	 Assert.notNull(userName, "userName Cannot be Null/Empty");
   	 Assert.notNull(accessId, "accessId cannot be Null");
        return this.userDAO.getFirmUsersbySubscriptionAndUserName(userName, accessId);
   }


}