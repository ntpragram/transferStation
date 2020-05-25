package com.ch.dao.master;

import org.springframework.stereotype.Repository;

@Repository
public interface VoiceRecordMapper {
    boolean manualVoice(Integer bid, String remark);
}