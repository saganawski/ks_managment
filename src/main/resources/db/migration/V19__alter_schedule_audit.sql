ALTER TABLE schedule_audit
ADD COLUMN is_payroll tinyInt(1) DEFAULT FALSE AFTER office_id;

