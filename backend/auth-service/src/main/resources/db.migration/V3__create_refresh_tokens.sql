CREATE TABLE refresh_tokens (
                                id BIGSERIAL PRIMARY KEY,
                                token_hash VARCHAR(255) UNIQUE NOT NULL,
                                user_id BIGINT NOT NULL,
                                device_id VARCHAR(255),
                                ip_address VARCHAR(45),
                                user_agent TEXT,
                                expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                is_revoked BOOLEAN DEFAULT FALSE,
                                revoked_at TIMESTAMP WITH TIME ZONE,
                                rotation_count INTEGER DEFAULT 0,
                                created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_tokens_hash ON refresh_tokens(token_hash);
CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_expires ON refresh_tokens(expires_at);
CREATE INDEX idx_refresh_tokens_revoked ON refresh_tokens(is_revoked);