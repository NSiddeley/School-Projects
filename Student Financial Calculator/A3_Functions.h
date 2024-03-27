#ifndef A2_FUNCTIONS_H_
#define A2_FUNCTIONS_H_

#define MAX_STRING_LEN 100
#define MAX_NUM_EXPENSES 10
#define MAX_FINANCE_RECORDS 10

#include <stdio.h>

typedef struct {
    // Data from user/file
    char username[MAX_STRING_LEN];
    int year;
    float savings;
    float grants;
    float weekly_pay;
    float annual_allowance;
    float rent;
    float utilities;
    int semesters;
    float tuition;
    float textbooks;
    int num_expenses;
    char expense_names[MAX_NUM_EXPENSES][MAX_STRING_LEN];
    float expense_costs[MAX_NUM_EXPENSES];

    // data calcuted from above
    float net_funds;
    float monthly_funds;
    float monthly_income;
    float monthly_budget;
    float essentials_total;

    int isValid; // 1 if data is valid, 0 otherwise
} FinanceData;

// --- New Functions:
//File IO
int splitLineIntoStrings(char line[], 
                        char words[][MAX_STRING_LEN], 
                        char seperators[]);

int readLine(char line[], int size, FILE* fp);
FinanceData readFinanceDataFromFile(char filename[]);
FinanceData readFinanceDataFromUser();
void saveFinanceRecordToFile(FinanceData fd, char filename[MAX_STRING_LEN]);

void getBudgetRules(int *essentials_ratio, int *wants_ratio, int *savings_ratio);

int selectFinanceData(FinanceData fds[], int numEntries);
void printFinances(FinanceData fd, int essentials_ratio, int wants_ratio, int savings_ratio);


// --- Old functions:

// Function Prototypes



int getIntInput(char prompt[]);
float getFloatInput(char prompt[]);
char getCharInput(char prompt[], char valid_inputs[], int num_valid);

float getWeeklyPay();
float getAnnualAllowance();
void getHousing(float *rent, float *utilities);
void getExpenses( char expense_names[MAX_NUM_EXPENSES][MAX_STRING_LEN], 
    			float expense_costs[MAX_NUM_EXPENSES],
    			int *num_expenses,
    			float *expenses_total);

void printTable(float savings, 
                float grants, 
                float net_funds, 
                float monthly_funds,
                float monthly_income, 
                float monthly_budget,  
                float rent,
                float utilities,
                char expense_names[MAX_NUM_EXPENSES][MAX_STRING_LEN], 
                float expense_costs[MAX_NUM_EXPENSES], 
                int num_expenses,
                float essentials_total, 
                float monthly_essentials_avail,
                float expensesRemaining,
                float monthly_wants, 
                float monthly_savings
                );
void printSummary(float expensesRemaining, float wantsAvailable, float savingsAvailable);

#endif
