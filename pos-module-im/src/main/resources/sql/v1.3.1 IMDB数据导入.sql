/*----------------------------------
----------执行前必读！！！----------
非开发环境下，当数据导入完成后需要再同步一次表结构，
以确保同步索引名称修改和个别字段的注释修改（以及IM之外手动修改的表结构）
----------------------------------*/


/* 将B端/业者所属的公司ID更新到im_user_info */
UPDATE im_user_info u
INNER JOIN merchant m ON m.user_id = u.user_id
SET u.company_id = m.company_id
WHERE u.user_type = 'b';

UPDATE im_user_info u
INNER JOIN employee e ON e.user_id = u.user_id
SET u.company_id = e.company_id
WHERE u.user_type = 'e';


/* 将业者所属的公司ID更新到im_session_member */
UPDATE im_session_member m
INNER JOIN employee e ON e.user_id = m.user_id
SET m.company_id = e.company_id
WHERE m.user_type = 'e';


/* 将所有公司数据写入im_company_info */
INSERT INTO im_company_info
(company_id, `name`, area_id, address, logo_image, create_time, update_time, available)
SELECT c.id, c.`name`, c.area_id, c.address, c.logo_image, NOW(), NOW(),
CASE c.deleted WHEN 1 THEN 0 ELSE c.available END
FROM company c;


/* 将会话关联的案例ID写入im_session_case */
INSERT INTO im_session_case(session_id, case_id)
SELECT se.id, se.case_id FROM im_session se;


/* 通话次数现在按成员统计，故无法同步旧数据，直接清零！*/
UPDATE im_session SET call_total = 0;


/* 全部数据导入后，删除im_session.case_id字段 */
ALTER TABLE im_session DROP COLUMN case_id;