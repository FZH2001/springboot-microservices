package com.example.fundtransferservice.model.mapper;

import com.example.fundtransferservice.model.dto.Transaction;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import org.springframework.beans.BeanUtils;

public class TransactionMapper  extends BaseMapper<TransactionEntity, Transaction>{
    @Override
    public TransactionEntity convertToEntity(Transaction dto, Object... args) {
        TransactionEntity entity = new TransactionEntity();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }
    @Override
    public Transaction convertToDto(TransactionEntity entity, Object... args) {
        Transaction dto = new Transaction();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
