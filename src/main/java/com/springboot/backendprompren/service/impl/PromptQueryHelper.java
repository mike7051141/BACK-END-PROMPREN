package com.springboot.backendprompren.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.springboot.backendprompren.data.entity.Condition;
import com.springboot.backendprompren.data.entity.QPrompt;

public class PromptQueryHelper {
    public static BooleanBuilder createFilterBuilder(Condition condition, String category, QPrompt prompt) {
        BooleanBuilder filterBuilder = new BooleanBuilder();

        addConditionFilters(condition, prompt, filterBuilder);

        addCategoryFilter(category, prompt, filterBuilder);


        return filterBuilder;
        
        }

    private static void addCategoryFilter(String category, QPrompt prompt, BooleanBuilder filterBuilder) {
        if (category != null && !category.isEmpty()) {
            filterBuilder.and(prompt.category.eq(category));
        }

    }

    private static void addConditionFilters(Condition condition, QPrompt prompt, BooleanBuilder filterBuilder) {
        if (condition != null) {
            // Condition에 따라 필터추가
            switch (condition) {
                case PRODUCTIVE:
                    filterBuilder.and(prompt.condition.eq(Condition.PRODUCTIVE));
                    break;
                case ARTISTIC:
                    filterBuilder.and(prompt.condition.eq(Condition.ARTISTIC));
                    break;
                case SYMBOLIC:
                    filterBuilder.and(prompt.condition.eq(Condition.SYMBOLIC));
                    break;
                case INTERESTING:
                    filterBuilder.and(prompt.condition.eq(Condition.INTERESTING));
                    break;
                default:
                    // 조건이 맞지 않으면 아무 필터도 추가하지 않음
                    break;
            }
        }
    }
}
