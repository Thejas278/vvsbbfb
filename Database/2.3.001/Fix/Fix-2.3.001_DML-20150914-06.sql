UPDATE AUTH_ACCESS SET ACTIVE ='N' WHERE ACCESS_CD = 'MOBILECMS_FREE_ACCESS'
UPDATE AUTH_ACCESS SET ACTIVE ='N' WHERE ACCESS_CD = 'MOBILECMS_ATTORNEY_ACCESS'
UPDATE AUTH_ACCESS SET ACTIVE ='N' WHERE ACCESS_CD = 'MOBILECMS_EMPLOYEE_ACCESS'
GO

UPDATE AUTH_USERS_ACCESS SET IS_ACCESS_OVERRIDDEN = 'Y', DATE_TIME_MOD = GETDATE(), MOD_USER_ID = 'SQL_SCRIPT'
WHERE ACCESS_ID IN (SELECT ID FROM AUTH_ACCESS WHERE ACCESS_CD = 'ARLINGTONGOV_FREE_ACCESS')
AND USER_ID IN (SELECT ID FROM AUTH_USERS WHERE EMAIL_ID IN ('bherbst@arlingtonva.us',
'draiden@arlingtonva.us',
'jbutler@arlingtonva.us',
'lcollier@arlingtonva.us',
'mcholmondeley@arlingtonva.us',
'tmccall@arlingtonva.us',
'tohora@arlingtonva.us',
'tcharris@arlingtonva.us',
'uarkin@arlingtonva.us',
'efeldm@arlingtonva.us',
'malbertson@arlingtonva.us',
'hpatel@arlingtonva.us',
'ssalang@fallschurchva.gov'))
AND IS_DELETED = 'N'
GO