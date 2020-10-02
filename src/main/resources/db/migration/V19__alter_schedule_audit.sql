ALTER TABLE schedule_audit
ADD COLUMN is_payroll tinyInt(1) DEFAULT FALSE AFTER office_id;

ALTER TABLE employee_schedule_payroll
ADD COLUMN overtime_minutes DECIMAL(6,2) DEFAULT NULL AFTER total_minutes;
