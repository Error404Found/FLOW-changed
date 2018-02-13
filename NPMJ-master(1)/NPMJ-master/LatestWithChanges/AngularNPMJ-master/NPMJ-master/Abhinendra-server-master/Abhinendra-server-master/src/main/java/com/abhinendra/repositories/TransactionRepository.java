package com.abhinendra.repositories;

import com.abhinendra.domain.Transaction;

import com.abhinendra.domain.QTransaction;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer>, QueryDslPredicateExecutor<Transaction>, QuerydslBinderCustomizer<QTransaction> {

    @Override
    default public void customize(QuerydslBindings bindings, QTransaction qTransaction) {
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }
    public  Transaction save(Transaction transaction);
}