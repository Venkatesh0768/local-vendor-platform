CREATE TABLE otp_records (
                             id BIGSERIAL PRIMARY KEY,
                             email VARCHAR(255) NOT NULL,
                             otp_hash VARCHAR(255) NOT NULL,
                             otp_type VARCHAR(50) NOT NULL, -- EMAIL_VERIFICATION, PASSWORD_RESET
                             attempts INTEGER DEFAULT 0,
                             max_attempts INTEGER DEFAULT 5,
                             expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
                             verified_at TIMESTAMP WITH TIME ZONE,
                             is_used BOOLEAN DEFAULT FALSE,
                             created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                             ip_address VARCHAR(45)
);

CREATE INDEX idx_otp_email_type ON otp_records(email, otp_type);
CREATE INDEX idx_otp_expires ON otp_records(expires_at);
CREATE INDEX idx_otp_used ON otp_records(is_used);
