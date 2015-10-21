import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fdt.common.util.SystemUtil;


public class Test {

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {

		/*String keystorePath = System.getProperty("javax.net.ssl.keyStore");
		String keystoreType = System.getProperty("javax.net.ssl.keyStoreType");
		String keystorePassword =System.getProperty("javax.net.ssl.keyStorePassword");


		System.out.println("javax.net.ssl.keyStore: " + keystorePath);
		System.out.println("javax.net.ssl.keyStoreType: " + keystoreType);
		System.out.println("javax.net.ssl.keyStorePassword: " + keystorePassword);

		Date date = SystemUtil.changeTimeZone(new Date(), TimeZone.getTimeZone("America/Los_Angeles"));

		System.out.println(date);*/

		 String allTransactionReferenceNumbers[] = {
				 "BL0PC7AD2B69",
				 "BL0PC7AE1938",
				 "BL0PC7AE19C9",
				 "BL0PC7B5F71F",
				 "BL0PC7B8E50A",
				 "BL0PC7B8E722",
				 "BL0PC7C59D42",
				 "BL0PC7C60872",
				 "BL0PC7CE2DB8",
				 "BL0PC7CEFC3E",
				 "BL1PC7AC069F",
				 "BL3PC7ACD9F4",
				 "BL3PC7AD7D6F",
				 "BP0PC486C14B",
				 "BP0PC4923F06",
				 "BP0PC4A358D4",
				 "BP0PC4A85F58",
				 "BP1PC48732A1",
				 "BP1PC4A6EF26",
				 "BP3PC4874A42",
				 "BP3PC49CBF46",
				 "BQ0PC5F64381",
				 "BQ0PC5F8448D",
				 "BQ0PC5F8DB50",
				 "BQ0PC6002BAA",
				 "BQ0PC603041E",
				 "BQ0PC60D361F",
				 "BQ0PC60D361F",
				 "BQ0PC617DD8D",
				 "BQ0PC61936CE",
				 "BQ1PC5F71639",
				 "BQ1PC5F71A44",
				 "BQ3PC5F6F129",
				 "BQ3PC5F74409",
				 "BR0PC62F7041",
				 "BR0PC63833B6",
				 "BR0PC63834C1",
				 "BR0PC6383730",
				 "BR0PC6383780",
				 "BR0PC645A05A",
				 "BR0PC6465215",
				 "BR0PC647D334",
				 "BR0PC64845B0",
				 "BR0PC6500AF7",
				 "BR0PC650339D",
				 "BR0PC6514DAA",
				 "BR1PC62E41C7",
				 "BR1PC62E513D",
				 "BR1PC62F1A7F",
				 "BR1PC62F2279",
				 "BR1PC645A1E0",
				 "BR3PC64486CF",
				 "BS0PD57E8A25",
				 "BS0PD57EBC7A",
				 "BS0PD57EC08C",
				 "BS0PD57F8CED",
				 "BS0PD5804400",
				 "BS0PD5813446",
				 "BS0PD5886DFB",
				 "BS0PD58B5394",
				 "BS0PD598163D",
				 "BS0PD5985DF1",
				 "BS0PD59B560E",
				 "BS0PD5A02BF1",
				 "BS0PD5A163F8",
				 "BS1PD58C6B89",
				 "BS3PD57F39B8",
				 "BS3PD57FE26B",
				 "BS3PD57FF525",
				 "BS3PD58A3D55",
				 "BS3PD594BE52",
				 "BT0PD77A9E49",
				 "BT0PD77A9EE8",
				 "BT0PD77BB86B",
				 "BT0PD77BC141",
				 "BT0PD7864D7F",
				 "BT0PD7876C84",
				 "BT0PD791F7DE",
				 "BT0PD7942FB6",
				 "BT0PD79490F5",
				 "BT0PD7977B6B",
				 "BT0PD79C524F",
				 "BT0PD79C64A4",
				 "BT0PD79D88CB",
				 "BT1PD77AA492",
				 "BT1PD77AB291",
				 "BT1PD77B714A",
				 "BT1PD7888242",
				 "BT1PD79B1315",
				 "BT3PD77B46A0",
				 "BT3PD77B5812",
				 "BU0PD6F5EE15",
				 "BU0PD6F67A55",
				 "BU0PD6F6D068",
				 "BU0PD6F6D2FB",
				 "BU0PD702AFE0",
				 "BU0PD70565D4",
				 "BU0PD70E51F8",
				 "BU0PD711A2F0",
				 "BU0PD711A52E",
				 "BU0PD7166263",
				 "BU1PD703CC60",
				 "BU3PD6F58BAA",
				 "BU3PD6F58E32",
				 "BU3PD7121270",
				 "BX0PD755D4A1",
				 "BX0PD756B77F",
				 "BX0PD757468F",
				 "BX0PD7609CB6",
				 "BX0PD76267F3",
				 "BX0PD76CF27C",
				 "BX0PD76F21C9",
				 "BX0PD76F681A",
				 "BX0PD7726724",
				 "BX0PD7726D77",
				 "BX0PD7789365",
				 "BX3PD7564738",
				 "BX3PD7614EF0",
				 "BX3PD76BD449"

		 };
		 
		 String consolidatedReferenceNumbers[] = {
				  "BR1PC62E513D",
				 "BT1PD77AB291",
				 "BT3PD77B46A0",
				 "BX3PD7564738",
				 "BP1PC48732A1",
				 "BQ3PC5F74409",
				 "BS0PD57F8CED",
				 "BT0PD77BB86B",
				 "BX0PD756B77F",
				 "BL0PC7AD2B69",
				 "BU0PD6F5EE15",
				 "BT0PD77BC141",
				 "BR0PC62F7041",
				 "BS3PD57FE26B",
				 "BL3PC7AD7D6F",
				 "BS3PD57FF525",
				 "BX0PD757468F",
				 "BU0PD6F67A55",
				 "BS0PD5804400",
				 "BQ0PC5F8DB50",
				 "BS0PD5813446",
				 "BQ0PC603041E",
				 "BX0PD76267F3",
				 "BS0PD58B5394",
				 "BT0PD7876C84",
				 "BL0PC7B8E50A",
				 "BL0PC7B8E722",
				 "BS1PD58C6B89",
				 "BT1PD7888242",
				 "BU0PD702AFE0",
				 "BU0PD70565D4",
				 "BS3PD594BE52",
				 "BP3PC49CBF46",
				 "BR3PC64486CF",
				 "BX3PD76BD449",
				 "BT0PD791F7DE",
				 "BR0PC645A05A",
				 "BR1PC645A1E0",
				 "BX0PD76CF27C",
				 "BR0PC6465215",
				 "BU0PD70E51F8",
				 "BR0PC647D334",
				 "BS0PD598163D",
				 "BX0PD76F21C9",
				 "BT0PD7942FB6",
				 "BL0PC7C59D42",
				 "BX0PD76F681A",
				 "BS0PD5985DF1",
				 "BT0PD79490F5",
				 "BR0PC64845B0",
				 "BL0PC7C60872",
				 "BS0PD59B560E",
				 "BX0PD7726724",
				 "BP0PC4A358D4",
				 "BU0PD711A2F0",
				 "BX0PD7726D77",
				 "BU0PD711A52E",
				 "BT0PD7977B6B",
				 "BU3PD7121270",
				 "BR0PC6514DAA",
				 "BX0PD7789365",
				 "BL0PC7CEFC3E",
				 "BQ0PC61936CE",
				 "BT0PD79D88CB",
				 "BS0PD5A163F8",
				 "BL0PC7CE2DB8",
				 "BR0PC650339D",
				 "BP0PC4A85F58",
				 "BT0PD79C64A4",
				 "BR0PC6500AF7",
				 "BT0PD79C524F",
				 "BS0PD5A02BF1",
				 "BT1PD79B1315",
				 "BP1PC4A6EF26",
				 "BU0PD7166263",
				 "BQ0PC617DD8D",
				 "BX0PD755D4A1",
				 "BS0PD57EC08C",
				 "BP0PC486C14B",
				 "BQ3PC5F6F129",
				 "BS3PD57F39B8",
				 "BT3PD77B5812",
				 "BU3PD6F58BAA",
				 "BU3PD6F58E32",
				 "BP3PC4874A42",
				 "BL3PC7ACD9F4",
				 "BR1PC62F1A7F",
				 "BT1PD77B714A",
				 "BQ1PC5F71639",
				 "BR1PC62F2279",
				 "BQ1PC5F71A44",
				 "BQ0PC60D361F",
				 "BQ0PC60D361F",
				 "BX0PD7609CB6",
				 "BT0PD7864D7F",
				 "BP0PC4923F06",
				 "BX3PD7614EF0",
				 "BS3PD58A3D55",
				 "BR0PC63833B6",
				 "BR0PC63834C1",
				 "BS0PD5886DFB",
				 "BQ0PC6002BAA",
				 "BL0PC7B5F71F",
				 "BR0PC6383730",
				 "BR0PC6383780",
				 "BT0PD77A9E49",
				 "BT0PD77A9EE8",
				 "BQ0PC5F64381",
				 "BS0PD57E8A25",
				 "BU0PD6F6D068",
				 "BQ0PC5F8448D",
				 "BU0PD6F6D2FB",
				 "BL0PC7AE1938",
				 "BL0PC7AE19C9",
				 "BU1PD703CC60"

		 };
	     
		 List<String> allTransactionReferenceNumbersList = Arrays.asList(allTransactionReferenceNumbers);
				 List<String> consolidatedReferenceNumbersList = Arrays.asList(consolidatedReferenceNumbers);

		 List<String> uncommon = new ArrayList<String> ();
		 for (String s : allTransactionReferenceNumbersList) {
		     if (!consolidatedReferenceNumbersList.contains(s)) uncommon.add(s);
		 }
		 for (String s : consolidatedReferenceNumbersList) {
		     if (!allTransactionReferenceNumbersList.contains(s)) uncommon.add(s);
		 }
		 
		 for (String s : uncommon) {
		     System.out.println(s);
		 }
	}

	public static Date getDate(Object object) {
        return object == null ? null :  (Date)((Timestamp)object);
    }

	public static Date getDateInTimezone(Date date, String timeZone) {

		DateTime dateTime = new DateTime(date);
    	DateTime dateTimeInTimezone = dateTime.withZone(DateTimeZone.forID(timeZone));
    	DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yyyy hh:mm a");
    	String dateAsString = format.print(dateTimeInTimezone);

    	System.out.println("dateTimeInTimezone: " + dateAsString);

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		sdf.setTimeZone(tz);
		Date newDate = null;
		try {
			newDate = sdf.parse(dateAsString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return newDate;
    }

	public static Date changeTimeZone(Date date, TimeZone zone) {
        Calendar first = Calendar.getInstance(zone);
        first.setTimeInMillis(date.getTime());
        Calendar output = Calendar.getInstance();
        output.set(Calendar.YEAR, first.get(Calendar.YEAR));
        output.set(Calendar.MONTH, first.get(Calendar.MONTH));
        output.set(Calendar.DAY_OF_MONTH, first.get(Calendar.DAY_OF_MONTH));
        output.set(Calendar.HOUR_OF_DAY, first.get(Calendar.HOUR_OF_DAY));
        output.set(Calendar.MINUTE, first.get(Calendar.MINUTE));
        output.set(Calendar.SECOND, first.get(Calendar.SECOND));
        output.set(Calendar.MILLISECOND, first.get(Calendar.MILLISECOND));

        return output.getTime();
    }

	/*public static Date getDateInTimezone(Date date, String timeZone) {
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		Calendar cal = Calendar.getInstance(tz);
		cal.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS zzz");
		format.setCalendar(cal);
		String dateAsString = format.format(cal.getTime());
		Date newDate = null;
		try {
			System.out.println(dateAsString);
			format.setTimeZone(TimeZone.getTimeZone(timeZone));
			newDate = format.parse(dateAsString);
		} catch (ParseException e) {
			System.out.println("Date Parsing Exception");
		}
		return newDate;
	}*/
}
