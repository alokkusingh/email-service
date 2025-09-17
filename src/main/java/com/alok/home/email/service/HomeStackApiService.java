package com.alok.home.email.service;

import com.alok.home.commons.dto.api.response.ExpensesMonthSumByCategoryResponse;
import com.alok.home.commons.dto.api.response.ExpensesResponseAggByDay;
import com.alok.home.commons.dto.api.response.InvestmentsResponse;
import com.alok.home.email.parser.dto.MonthInvestmentRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class HomeStackApiService {

    private final RestTemplate homeApiRestTemplate;
    private final String apiBaseUrl;

    public HomeStackApiService(RestTemplate homeApiRestTemplate, @Value("${home-api.url}") String apiBaseUrl) {
        this.homeApiRestTemplate = homeApiRestTemplate;
        this.apiBaseUrl = apiBaseUrl;
    }

    // URL: https://hdash.alok.digital/home/api/expense/sum_by_category_month
    // This Month Expense by category
    // Previous Month Expense By Category
    // This Month Total Expense
    // Previous Month Total Expense
    // This Year Total Expense
    // Previous Year Total Expense

    // URL: https://hdash.alok.digital/home/api/expense/current_month_by_day
    // Yeterday's Expense

    public Map<String, Object> getDailySummaryReportData() {

        NumberFormat df = new DecimalFormat("##,##,###");

        Map<String, Object> response = new HashMap<>();
        try {
            ExpensesMonthSumByCategoryResponse expensesMonthSumByCategoryResponse = homeApiRestTemplate
                    .getForEntity(apiBaseUrl + "/expense/sum_by_category_month", ExpensesMonthSumByCategoryResponse.class)
                    .getBody();

            LocalDate today = LocalDate.now();
            response.put("thisMonthSpendCategories", expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(today.getYear()))
                    .filter(expenseCategoryMonthSum -> expenseCategoryMonthSum.getMonth().equals(today.getMonth().getValue()))
                    .map(expenseCategoryMonthSum -> {
                        Map<String, Object> expenseCategoryMap = new HashMap<>();
                        expenseCategoryMap.put("category", expenseCategoryMonthSum.getCategory());
                        expenseCategoryMap.put("amount", df.format(Math.round(expenseCategoryMonthSum.getSum())));
                        return expenseCategoryMap;
                    })
                    .toList());

            response.put("lastMonthSpendCategories", expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(today.getYear()))
                    .filter(expenseCategoryMonthSum -> expenseCategoryMonthSum.getMonth().equals(today.getMonth().getValue()-1))
                    .map(expenseCategoryMonthSum -> {
                        Map<String, Object> expenseCategoryMap = new HashMap<>();
                        expenseCategoryMap.put("category", expenseCategoryMonthSum.getCategory());
                        expenseCategoryMap.put("amount", df.format(Math.round(expenseCategoryMonthSum.getSum())));
                        return expenseCategoryMap;
                    })
                    .toList());

            response.put("thisMonthTotalExpense", df.format(Math.round(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(today.getYear()))
                    .filter(expenseCategoryMonthSum -> expenseCategoryMonthSum.getMonth().equals(today.getMonth().getValue()))
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .reduce(0d, Double::sum))));

            response.put("previousMonthTotalExpense", df.format(Math.round(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(today.getYear()))
                    .filter(expenseCategoryMonthSum -> expenseCategoryMonthSum.getMonth().equals(today.getMonth().getValue()-1))
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .reduce(0d, Double::sum))));

            response.put("thisYearTotalExpense", df.format(Math.round(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(today.getYear()))
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .reduce(0d, Double::sum))));

            response.put("previousYearTotalExpense", df.format(Math.round(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(today.getYear() - 1))
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .reduce(0d, Double::sum))));

            ExpensesResponseAggByDay expensesResponseAggByDay = homeApiRestTemplate
                    .getForEntity(apiBaseUrl + "/expense/current_month_by_day", ExpensesResponseAggByDay.class)
                    .getBody();

            response.put("yesterdayExpense", df.format(Math.round(expensesResponseAggByDay.getExpenses().stream()
                    .filter(expenseAggByDay -> expenseAggByDay.getDate().equals(today.minusDays(1)))
                    .map(ExpensesResponseAggByDay.DayExpense::getAmount)
                    .reduce(0d, Double::sum))));
        } catch (RuntimeException rte) {
            rte.printStackTrace();
            System.out.println("Failed to send email, error: "  + rte.getMessage());
        }

        return response;
    }

    // URL: https://hdash.alok.digital/home/api/investment/all
    // Investments as on month
    // Return on Investments as on month
    // Investments value as on month

    public Map<String, Object> getInvestmentReportDataForCurrentMonthAndPreviousMonth(YearMonth yearMonth) {

        Map<String, Object> response = new HashMap<>();

        response.put("currentMonthInvestments", getMonthInvestmentRecord(yearMonth));
        response.put("previousMonthInvestments", getMonthInvestmentRecord(yearMonth.minusMonths(1)));

        return response;
    }

    private List<Map<String, Object>> getMonthInvestmentRecord(YearMonth yearMonth) {

        try {
            InvestmentsResponse investmentsResponse = homeApiRestTemplate
                    .getForEntity(apiBaseUrl + "/investment/all", InvestmentsResponse.class)
                    .getBody();

            List<MonthInvestmentRecord> currentMonthInvestments = new ArrayList<>(investmentsResponse.getMonthInvestments().stream()
                    .filter(monthInvestments -> monthInvestments.getYearMonth().equals(yearMonth.toString()))
                    .flatMap(monthInvestments -> monthInvestments.getInvestments().stream())
                    .map(monthInvestment -> MonthInvestmentRecord.builder()
                            .head(monthInvestment.getHead())
                            .asOnInvestment(monthInvestment.getAsOnInvestment())
                            .asOnValue(monthInvestment.getAsOnValue())
                            .monthInvestment(monthInvestment.getInvestmentAmount())
                            .build()
                    )
                    .toList());

            YearMonth previousYearMonth = yearMonth.minusMonths(1);
            List<MonthInvestmentRecord> previousMonthInvestments = investmentsResponse.getMonthInvestments().stream()
                    .filter(monthInvestments -> monthInvestments.getYearMonth().equals(previousYearMonth.toString()))
                    .flatMap(monthInvestments -> monthInvestments.getInvestments().stream())
                    .map(monthInvestment -> MonthInvestmentRecord.builder()
                            .head(monthInvestment.getHead())
                            .asOnValue(monthInvestment.getAsOnValue())
                            .build()
                    )
                    .toList();

            // Set Current Month Return from previous month
            currentMonthInvestments.stream()
                    .map(investmentRecord -> {
                        investmentRecord.setMonthReturn(investmentRecord.getAsOnValue() - previousMonthInvestments.stream()
                                .filter(previousMonthInvestmentRecord -> previousMonthInvestmentRecord.getHead().equals(investmentRecord.getHead()))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Previous month investment not found"))
                                .getAsOnValue() - investmentRecord.getMonthInvestment());
                        return investmentRecord;
                    })
                    .toList();

            // Set Total Investment Record
            MonthInvestmentRecord totalInvestmentRecord = MonthInvestmentRecord.builder()
                    .head("Total")
                    .asOnInvestment(currentMonthInvestments.stream()
                            .map(MonthInvestmentRecord::getAsOnInvestment)
                            .reduce(0, Integer::sum))
                    .asOnValue(currentMonthInvestments.stream()
                            .map(MonthInvestmentRecord::getAsOnValue)
                            .reduce(0, Integer::sum))
                    .monthInvestment(currentMonthInvestments.stream()
                            .map(MonthInvestmentRecord::getMonthInvestment)
                            .reduce(0, Integer::sum))
                    .monthReturn(currentMonthInvestments.stream()
                            .map(MonthInvestmentRecord::getMonthReturn)
                            .reduce(0, Integer::sum))
                    .build();

            currentMonthInvestments.add(totalInvestmentRecord);

            return currentMonthInvestments.stream()
                    .map(investmentRecord -> {
                        Map<String, Object> headAttributes = new HashMap<>();
                        headAttributes.put("head", investmentRecord.getHead());
                        headAttributes.put("asOnInvestment", investmentRecord.getAsOnInvestment());
                        headAttributes.put("asOnValue", investmentRecord.getAsOnValue());
                        headAttributes.put("monthInvestment", investmentRecord.getMonthInvestment());
                        headAttributes.put("monthReturn", investmentRecord.getMonthReturn());
                        return headAttributes;
                    })
                    .toList();

        } catch (RuntimeException rte) {
            rte.printStackTrace();
            System.out.println("Failed to send email, error: "  + rte.getMessage());
        }

        return Collections.emptyList();
    }
}
