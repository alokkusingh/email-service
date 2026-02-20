package com.alok.home.email.service;

import com.alok.home.commons.dto.api.response.ExpensesMonthSumByCategoryResponse;
import com.alok.home.commons.dto.api.response.ExpensesResponseAggByDay;
import com.alok.home.commons.dto.api.response.InvestmentsResponse;
import com.alok.home.email.parser.dto.MonthInvestmentRecord;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Slf4j
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
            log.debug("ExpensesMonthSumByCategoryResponse: {}", expensesMonthSumByCategoryResponse);

            YearMonth currentYearMonth = YearMonth.now();
            response.put("thisMonthSpendCategories", expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(currentYearMonth.getYear()))
                    .filter(expenseCategoryMonthSum -> expenseCategoryMonthSum.getMonth().equals(currentYearMonth.getMonth().getValue()))
                    .map(expenseCategoryMonthSum -> {
                        Map<String, Object> expenseCategoryMap = new HashMap<>();
                        expenseCategoryMap.put("category", expenseCategoryMonthSum.getCategory());
                        expenseCategoryMap.put("amount", df.format(Math.round(expenseCategoryMonthSum.getSum())));
                        return expenseCategoryMap;
                    })
                    .toList());

            YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
            response.put("lastMonthSpendCategories", expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(previousYearMonth.getYear()))
                    .filter(expenseCategoryMonthSum -> expenseCategoryMonthSum.getMonth().equals(previousYearMonth.getMonth().getValue()))
                    .map(expenseCategoryMonthSum -> {
                        Map<String, Object> expenseCategoryMap = new HashMap<>();
                        expenseCategoryMap.put("category", expenseCategoryMonthSum.getCategory());
                        expenseCategoryMap.put("amount", df.format(Math.round(expenseCategoryMonthSum.getSum())));
                        return expenseCategoryMap;
                    })
                    .toList());

            response.put("thisMonthTotalExpense", df.format(Math.round(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(currentYearMonth.getYear()))
                    .filter(expenseCategoryMonthSum -> expenseCategoryMonthSum.getMonth().equals(currentYearMonth.getMonth().getValue()))
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .reduce(0d, Double::sum))));

            response.put("previousMonthTotalExpense", df.format(Math.round(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(previousYearMonth.getYear()))
                    .filter(expenseCategoryMonthSum -> expenseCategoryMonthSum.getMonth().equals(previousYearMonth.getMonth().getValue()))
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .reduce(0d, Double::sum))));

            response.put("thisYearTotalExpense", df.format(Math.round(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(currentYearMonth.getYear()))
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .reduce(0d, Double::sum))));

            response.put("previousYearTotalExpense", df.format(Math.round(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(currentYearMonth.getYear() - 1))
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .reduce(0d, Double::sum))));

            ExpensesResponseAggByDay expensesResponseAggByDay = homeApiRestTemplate
                    .getForEntity(apiBaseUrl + "/expense/current_month_by_day", ExpensesResponseAggByDay.class)
                    .getBody();
            log.debug("ExpensesResponseAggByDay: {}", expensesResponseAggByDay);

            LocalDate today = LocalDate.now();
            response.put("yesterdayExpense", df.format(Math.round(expensesResponseAggByDay.getExpenses().stream()
                    .filter(expenseAggByDay -> expenseAggByDay.getDate().equals(today.minusDays(1)))
                    .map(ExpensesResponseAggByDay.DayExpense::getAmount)
                    .reduce(0d, Double::sum))));
        } catch (RuntimeException rte) {
            rte.printStackTrace();
            log.error("Failed to read expense details and parse, error: {}",  rte.getMessage());
        }

        return response;
    }

    // URL: https://hdash.alok.digital/home/api/expense/sum_by_category_year?year=2025
    // This Year Expense by category
    // Previous Year Expense by category
    // This Year Expense for each Category
    // This year Expense by month

    public Map<String, Object> getYearlyExpenseReportDataForYear(int year) {

        Map<String, Object> response = new HashMap<>();
        response.put("year", year);
        response.put("previousYear", year -1);

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

        try {
            Map<String, Pair<Integer, Integer>> categoryYearsExpenseMap = new HashMap<>();

            ExpensesMonthSumByCategoryResponse expensesMonthSumByCategoryResponse = homeApiRestTemplate
                    .getForEntity(apiBaseUrl + "/expense/sum_by_category_year?year=" + year, ExpensesMonthSumByCategoryResponse.class)
                    .getBody();
            log.debug("ExpensesMonthSumByCategoryResponse: {}", expensesMonthSumByCategoryResponse);

            ExpensesMonthSumByCategoryResponse previousYearExpensesMonthSumByCategoryResponse = homeApiRestTemplate
                    .getForEntity(apiBaseUrl + "/expense/sum_by_category_year?year=" + (year -1), ExpensesMonthSumByCategoryResponse.class)
                    .getBody();
            log.debug("Previous Year ExpensesMonthSumByCategoryResponse: {}", previousYearExpensesMonthSumByCategoryResponse);

            Optional.ofNullable(Optional.ofNullable(expensesMonthSumByCategoryResponse)
                            .orElse(ExpensesMonthSumByCategoryResponse.builder().build()).getExpenseCategorySums())
                    .orElse(Collections.emptyList())
                    .forEach(expenseCategoryYearSum -> {
                        categoryYearsExpenseMap.putIfAbsent(expenseCategoryYearSum.getCategory(), Pair.with(0,0));
                        categoryYearsExpenseMap.compute(expenseCategoryYearSum.getCategory(),
                                (k, yearSums) -> Pair.with(
                                        yearSums.getValue0() + Optional.ofNullable(expenseCategoryYearSum.getSum()).orElse(0d).intValue(),
                                        yearSums.getValue1()
                                ));
                    });

            Optional.ofNullable(Optional.ofNullable(previousYearExpensesMonthSumByCategoryResponse)
                            .orElse(ExpensesMonthSumByCategoryResponse.builder().build()).getExpenseCategorySums())
                    .orElse(Collections.emptyList())
                    .forEach(expenseCategoryYearSum -> {
                        categoryYearsExpenseMap.putIfAbsent(expenseCategoryYearSum.getCategory(), Pair.with(0,0));
                        categoryYearsExpenseMap.compute(expenseCategoryYearSum.getCategory(),
                                (k, yearSums) -> Pair.with(
                                        yearSums.getValue0(),
                                        yearSums.getValue1() + Optional.ofNullable(expenseCategoryYearSum.getSum()).orElse(0d).intValue()
                                ));
                    });

            record CategoryYearAndPreviousYearExpense(String category, String yearExpense, String prevYearExpense) {}
            List<CategoryYearAndPreviousYearExpense> categoryYearAndPreviousYearExpenseList = categoryYearsExpenseMap.entrySet()
                    .stream()
                    .map(entry -> new CategoryYearAndPreviousYearExpense(
                            entry.getKey(),
                            currencyFormatter.format(entry.getValue().getValue0()),
                            currencyFormatter.format(entry.getValue().getValue1())
                    ))
                    .sorted(Comparator.comparing(CategoryYearAndPreviousYearExpense::category))
                    .toList();

            response.put("categoryYearAndPreviousYearExpenseList", categoryYearAndPreviousYearExpenseList);

            // format sum in INR format without decimal
            response.put("thisYearTotalExpense", currencyFormatter.format(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .map(Double::intValue)
                    .reduce(0, Integer::sum)));

            response.put("previousYearTotalExpense", currencyFormatter.format(previousYearExpensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .map(Double::intValue)
                    .reduce(0, Integer::sum)));

        } catch (RuntimeException rte) {
            rte.printStackTrace();
            log.error("Failed to read expense details and parse, error: {}",  rte.getMessage());
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

            log.debug("InvestmentsResponse: {}", investmentsResponse);

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
            log.error("Failed to send email, error: {}",  rte.getMessage());
        }

        return Collections.emptyList();
    }
}
