CREATE TABLE password_reset_tokens (
                                       id BIGSERIAL PRIMARY KEY,
                                       token_hash VARCHAR(255) UNIQUE NOT NULL,
                                       user_id BIGINT NOT NULL,
                                       expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                       is_used BOOLEAN DEFAULT FALSE,
                                       used_at TIMESTAMP WITH TIME ZONE,
                                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                       ip_address VARCHAR(45),
                                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_password_reset_token ON password_reset_tokens(token_hash);
CREATE INDEX idx_password_reset_user ON password_reset_tokens(user_id);
CREATE INDEX idx_password_reset_expires ON password_reset_tokens(expires_at);

-- Audit log table for security events
CREATE TABLE security_audit_logs (
                                     id BIGSERIAL PRIMARY KEY,
                                     user_id BIGINT,
                                     event_type VARCHAR(100) NOT NULL, -- LOGIN_SUCCESS, LOGIN_FAILED, TOKEN_REFRESH, LOGOUT, etc.
                                     ip_address VARCHAR(45),
                                     user_agent TEXT,
                                     details JSONB,
                                     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_audit_logs_user ON security_audit_logs(user_id);
CREATE INDEX idx_audit_logs_event ON security_audit_logs(event_type);
CREATE INDEX idx_audit_logs_created ON security_audit_logs(created_at);

-- Rate limiting table
CREATE TABLE rate_limit_records (
                                    id BIGSERIAL PRIMARY KEY,
                                    identifier VARCHAR(255) NOT NULL, -- email or IP address
                                    action_type VARCHAR(50) NOT NULL, -- OTP_REQUEST, LOGIN_ATTEMPT, etc.
                                    attempt_count INTEGER DEFAULT 1,
                                    window_start TIMESTAMP WITH TIME ZONE NOT NULL,
                                    last_attempt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                    UNIQUE(identifier, action_type, window_start)
);

CREATE INDEX idx_rate_limit_identifier ON rate_limit_records(identifier, action_type);
CREATE INDEX idx_rate_limit_window ON rate_limit_records(window_start);