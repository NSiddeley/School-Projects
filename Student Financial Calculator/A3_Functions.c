/************************** A3_Functions.c **************************
Student Name: Nigel Siddeley       Student ID: 1170276
Due Date: Monday, 4th Dec 2023, at 11:59 pm
Course Name: CIS*1500
I have exclusive control over this submission via my password.
By including this statement in this header comment, I certify that:
1) I have read and understood the University policy on academic integrity; and
2) I have completed assigned video on academic integrity.
I assert that this work is my own. I have appropriately acknowledged any and all material (code, data, images, ideas or words) that I have used, whether directly quoted or paraphrase. Furthermore, I certify that this assignment was prepared by me specifically for this course.
***************************************************************
This file contains…

All the functions in this file are called in the main for the Student Financial Calculator program in order to store and manipulate financial data

Function selectFinanceData(FinanceData[], int):
Prompts user to select a Finance Data to view. Prints a numbered list of Finance Data which is passed in from the main for the program. Returns the selected index converted to 0-indexing (int)

Function readFinanceDataFromFile(char[]):
Reads finance data from a given csv file and populates the members of a Finance Data Struct. Returns a populated finance struct if the file exists. If the file doesn't exists an error msg is given

Function readFinanceDataFromUser():
Prompts user for the information needed to populate a Finance Data struct. Returns struct  populated with the information entered by the user

Function getBudgetRules(int*, int*, int*):
Prompts the user for new budgeting rules and updates their values in memory. Budgeting rules are stored in variables initialized in main

Function saveFinanceRecordToFile(FinanceData, char[]):
writes the information from a given FinanceData struct to a csv file

Function printFinances(FinanceData, int, int, int):
prints a table and summary of the given Finance Data based off of the current budgeting rules

This program also contains other file IO functions and functions from A2

***************************************************************
The program should be compiled using the following flags:
-std=c99
-Wall
-lm
Compiling:
gcc -Wall -std=c99 1170276A3.c A3_Functions.c -o A3 -lm
OR
gcc -Wall -std=c99 1170276A3.c A3_Functions.c -lm
Running the program:
./A3
OR
./a.out
***************************************************************/

#include "A3_Functions.h"

#include <math.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>

int selectFinanceData(FinanceData fds[], int numEntries) {

    // Prompts user to select a Finance Data to view
    // Prints a numbered list of stored Finance Data
    printf("Select finances to view:\n");
    for(int i=0; i<numEntries; i++){
      printf("%d) %s %d\n", i+1, fds[i].username, fds[i].year);
    }

    // Variable for storing user's input
    int input;

    // Prompts user to enter their choice.
    // If they enter an invalid index they are given an error msg and prompted again
    do{
      printf("\nEnter your choice: ");
      fscanf(stdin, "%d", &input);

      if(input < 1 || input > numEntries){
        printf("Invalid input.\n");
      }
    } while((input < 1 || input > numEntries));
    

    // Returns the user index input converted to 0-indexing
    return input - 1;
    
}

FinanceData readFinanceDataFromFile(char filename[]) {
    // Initializing Finance Data struct
    FinanceData fd;

    // Opening file
    FILE *fp;
    fp = fopen(filename, "r");

    // Checks if the given file exists and updates the isValid field of the struct accordingly
    if(fp==NULL){
      fd.isValid=0;
      return fd;
    }else{
      fd.isValid=1;
    }
  
    // Temporary arrays used for reading and splitting lines from the file
    char line[MAX_STRING_LEN*MAX_NUM_EXPENSES];
    char words[MAX_NUM_EXPENSES+1][MAX_STRING_LEN];
    char info[12][MAX_STRING_LEN];
  
    // Reads first 12 lines (all info except for expense names/costs) of file and stores it in info array 
    for(int i=0; i<12; i++){
      readLine(line, MAX_STRING_LEN*2 + 1, fp);
      splitLineIntoStrings(line, words, ",");
      strcpy(info[i], words[1]);
    }
  
    // Populating data fields of Finance Data struct
    strcpy(fd.username, info[0]);
    fd.year = atoi(info[1]);
    fd.savings = atof(info[2]);
    fd.grants = atof(info[3]);
    fd.weekly_pay = atof(info[4]);
    fd.annual_allowance = atof(info[5]);
    fd.rent = atof(info[6]);
    fd.utilities = atof(info[7]);
    fd.semesters = atoi(info[8]);  
    fd.tuition = atof(info[9]);
    fd.textbooks = atof(info[10]);
    fd.num_expenses = atoi(info[11]);


    // Reads expense names from the file
    readLine(line, MAX_STRING_LEN*MAX_NUM_EXPENSES + MAX_NUM_EXPENSES, fp);
    splitLineIntoStrings(line, words, ",");

    // Populating the fields of the struct
    for(int i=0; i<fd.num_expenses; i++){
      strcpy(fd.expense_names[i], words[i+1]);
    }

    // Reads expense costs from the file
    readLine(line, MAX_STRING_LEN*MAX_NUM_EXPENSES + MAX_NUM_EXPENSES, fp);
    splitLineIntoStrings(line, words, ",");

    // Populating the fields of the struct
    for(int i=0; i<fd.num_expenses; i++){
      fd.expense_costs[i] = atof(words[i+1]);
    }

    // Calculating/populating remaining fields of the struct
    fd.net_funds = fd.savings + fd.grants - (fd.tuition*fd.semesters) - (fd.textbooks*fd.semesters);

    fd.monthly_funds = fd.net_funds / 12;
    fd.monthly_income = ((fd.weekly_pay*52) + fd.annual_allowance) / 12;
    fd.monthly_budget = fd.monthly_funds + fd.monthly_income;
  
    fd.essentials_total=0;
    for(int i=0; i<fd.num_expenses; i++){
      fd.essentials_total += fd.expense_costs[i];
    }
      fd.essentials_total += fd.rent + fd.utilities;

    // Closing the file
    fclose(fp);
  
    return fd; // Return the populated struct
}

FinanceData readFinanceDataFromUser(){
    // Initializing variables that will be used to store finance data entered by user
    FinanceData fd;
  
    // Prompts user for name
    printf("\nWhat is your name? ");
    fscanf(stdin, "%s", fd.username);

    // Prompts user for year
    fd.year=getIntInput("What year does this finance data represent? ");

    // Prompts user for savings
    fd.savings = getFloatInput("How much savings do you have set aside for the year? ");

    // Prompts user for grants
    fd.grants = getFloatInput("Total funds from Grants/Loans/Scholarships/Gifts? ");

    // Gets weekly pay
    fd.weekly_pay = getWeeklyPay();

    // Gets annual allowance
    fd.annual_allowance = getAnnualAllowance();

    // Gets rent/utilities
    getHousing(&(fd.rent), &(fd.utilities));
  
    // Prompts user for semesters
    fd.semesters = getIntInput("How many semesters will you attend school this year? ");

    // Prompts user for tuition
    fd.tuition = getFloatInput("What does tuition cost per semester? ");

    // Prompts user for textbooks
    fd.textbooks = getFloatInput("How much do you expect to pay for textbooks per semester? ");

    // Gets number of expenses and expenses names/costs/total
    fd.num_expenses=0;
    fd.essentials_total=0.0;
    getExpenses(fd.expense_names, fd.expense_costs, &(fd.num_expenses), &(fd.essentials_total));
    fd.essentials_total += fd.rent + fd.utilities;

    // Calculating/populating remaining fields of the struct
    fd.net_funds = fd.savings + fd.grants - (fd.tuition*fd.semesters) - (fd.textbooks*fd.semesters);

    fd.monthly_funds = fd.net_funds / 12;
    fd.monthly_income = ((fd.weekly_pay*52) + fd.annual_allowance) / 12;
    fd.monthly_budget = fd.monthly_funds + fd.monthly_income;

    fd.isValid=1;

    return fd; // Return the populated struct

}

void getBudgetRules(int *essentials_ratio, int *wants_ratio, int *savings_ratio) {
    do{
      // Prompts the user to enter budgeting ratios
      *essentials_ratio = getIntInput("Enter essentials ratio (ex. 50): ");
      *wants_ratio = getIntInput("Enter wants ratio (ex. 30): ");
      *savings_ratio = getIntInput("Enter savings ratio (ex. 20): ");

      // If the ratios do not add up to 100 user is given an error message and they are prompted again
      if((*essentials_ratio + *wants_ratio + *savings_ratio) != 100){
        printf("Ratios must add up to 100.");
      }
    } while ((*essentials_ratio + *wants_ratio + *savings_ratio) != 100);

}


void saveFinanceRecordToFile(FinanceData fd, char filename[MAX_STRING_LEN]) {
    // Opening file, if file already exists it will be overwritten
    FILE *fp;
    fp = fopen(filename, "w");

    // Printing fields of the given Financial Data to a file with the given filename
    fprintf(fp, "username,%s\n", fd.username);
    fprintf(fp, "year,%d\n", fd.year);
    fprintf(fp, "savings,%.0f\n", fd.savings);
    fprintf(fp, "grants,%.0f\n", fd.grants);
    fprintf(fp, "weekly_pay,%.0f\n", fd.weekly_pay);
    fprintf(fp, "annual_allowance,%.0f\n", fd.annual_allowance);
    fprintf(fp, "rent,%.0f\n", fd.rent);
    fprintf(fp, "utilities,%.0f\n", fd.utilities);
    fprintf(fp, "semesters,%d\n", fd.semesters);
    fprintf(fp, "tuition,%.0f\n", fd.tuition);
    fprintf(fp, "textbooks,%.0f\n", fd.textbooks);
    fprintf(fp, "expense_num,%d\n", fd.num_expenses);

    // Printing expense names to file
    fprintf(fp, "expense_name,");
    for(int i=0; i<fd.num_expenses; i++){
      if(i==fd.num_expenses-1){
        fprintf(fp, "%s\n", fd.expense_names[i]);
      }else{
        fprintf(fp, "%s,", fd.expense_names[i]);
      }
    }

    // Printing expense costs to file
    fprintf(fp, "expense_cost,");
    for(int i=0; i<fd.num_expenses; i++){
      if(i==fd.num_expenses-1){
        fprintf(fp, "%.0f\n", fd.expense_costs[i]);
      }else{
        fprintf(fp, "%.0f,", fd.expense_costs[i]);
      }
    }

    // Closes file
    fclose(fp);
    // Feedback msg to let user know data has been stored
    printf("Finance data saved to %s\n", filename);
}

void printFinances(FinanceData fd, int essentials_ratio, int wants_ratio, int savings_ratio) {
    // Calculations for funds available based on budgeting ratios
    float monthly_essentials_avail = fd.monthly_budget * essentials_ratio/100;
    float monthly_wants_avail = fd.monthly_budget * wants_ratio/100;
    float monthly_savings_avail = fd.monthly_budget * savings_ratio/100;
    float expenses_remaining = monthly_essentials_avail - fd.essentials_total;

    // Prints table of the given Financial Data
    printTable(fd.savings, 
               fd.grants, 
               fd.net_funds, 
               fd.monthly_funds,
               fd.monthly_income, 
               fd.monthly_budget,  
               fd.rent,
               fd.utilities,
               fd.expense_names, 
               fd.expense_costs, 
               fd.num_expenses,
               fd.essentials_total, 
               monthly_essentials_avail,
               expenses_remaining,
               monthly_wants_avail, 
               monthly_savings_avail
               );

    // Prints summary of the given Financial Data
    printSummary(expenses_remaining, monthly_wants_avail, monthly_savings_avail);
    
}

/**** Completed Functions Below ****/
/**** You may use these without modification ****/


/* Completed Functions From Class*/
/* You may use these without modification, or create your own*/

int splitLineIntoStrings(char line[], 
                        char words[][MAX_STRING_LEN], 
                        char seperators[]){
    int wordCount = 0;
    char *token = strtok(line, seperators);

        while (token != NULL){
            strcpy(words[wordCount], token);
            token = strtok(NULL, seperators);
            wordCount++;
        }

    return wordCount;
}

int readLine(char line[], int size, FILE* fp){
    // check that the file exists, and we are not at the end
    if(fp == NULL || feof(fp)){ 
        return 0;
    }

    // read the next line of the file into the string line
	fgets(line, size, fp); 

	// fgets leaves a '\n' at the end of the string, the next line will remove it.
	line[strlen(line) -1] = '\0';

    return 1;
}


/* Completed Functions From A2*/
/* You may use these without modification, or re-use your own*/
/* Note: the printTable and printSummary are slightly modified.*/
/* The only change is that they do not specify the budget rules anymore. */


float getAnnualAllowance(){
    float allowance;
    char valid_y_n[] = {'Y','N'};
    char valid_frequencies[] = {'W','M','A'};
    char receive_allowance = getCharInput("Do you receive an allowance? (Y or N) ", valid_y_n, 2);
    
    if (receive_allowance == 'Y' || receive_allowance == 'y') {
        char allowance_frequency = getCharInput("\tHow often do you receive an allowance? (W for Weekly, M for Monthly, A for annually): ", valid_frequencies, 6);
        allowance = getFloatInput("\tHow much do you receive per allowance payment? $ ");
        if (allowance_frequency == 'W' || allowance_frequency == 'w') {
            allowance *= 52;  // Convert weekly allowance to annual
        } else if (allowance_frequency == 'M' || allowance_frequency == 'm') {
            allowance *= 12.0;  // Convert monthly allowance to annual
        }

    } else {
        allowance = 0.0;
    }

    return allowance;

}

float getWeeklyPay(){
    float job_income;
    int hours_per_week;
    char valid_y_n[] = {'Y','N'};
    char part_time_job = getCharInput("Do you have a part-time job? (Y or N) ", valid_y_n, 2);
    
    if (part_time_job == 'Y' || part_time_job =='y' ) {
        job_income = getFloatInput("\tHow much do you make per hour? $ ");
        hours_per_week = getIntInput("\tHow many hours do you work per week? ");
    } else {
        job_income = 0.0;
        hours_per_week = 0;
    }

    return job_income * hours_per_week;
}

void getHousing(float *rent, float *utilities){
    char valid_y_n[] = {'Y','N'};
    char pay_housing = getCharInput("Do you pay for Housing? (Y or N) ", valid_y_n, 2);
    if (pay_housing == 'Y' || pay_housing == 'y') {
        *rent = getFloatInput("\tWhat is your monthly housing payment? $ ");
        *utilities = getFloatInput("\tUtilities cost per month? $ ");

    } else {
        *rent = 0.0;
        *utilities = 0.0;
    }
}

void getExpenses( char expense_names[MAX_NUM_EXPENSES][MAX_STRING_LEN],
    				float expense_costs[MAX_NUM_EXPENSES],
    				int *num_expenses,
    				float *expenses_total){
    printf("\nExpenses:\n");
    while(*num_expenses < MAX_NUM_EXPENSES){ 
        printf("\n\tEnter a one-word name of an expense, if you are finished enter \"finished\": ");
        scanf(" %s", expense_names[*num_expenses]);
        
        if(strcmp(expense_names[*num_expenses], "finished")==0)
            break;
            
        expense_costs[*num_expenses] = getFloatInput("\tWhat is the monthly cost of this expense? $ ");
        *expenses_total += expense_costs[*num_expenses];

        *num_expenses = *num_expenses+1;
    }
}

char getCharInput(char prompt[], char valid_inputs[], int num_valid){
    char value;
    int valid = 0;
    do {
        printf("\n%s", prompt);
        scanf(" %c", &value);

        valid = 0;
        for(int i = 0; i < num_valid; i++){
            if(toupper(value) == toupper(valid_inputs[i]))
                valid = 1;
        }

        if(!valid) printf("Invalid Input.\n");

    } while (!valid);
    return value;
}

int getIntInput(char prompt[]) {
    int value;
    do {
        printf("\n%s", prompt);
        scanf("%d", &value);
        if (value < 0)
            printf("Invalid input.\n");
    } while (value < 0);
    return value;
}

float getFloatInput(char prompt[]) {
    float value;
    do {
        printf("\n%s", prompt);
        scanf("%f", &value);
        if (value < 0)
            printf("Invalid input.\n");
    } while (value < 0);
    return value;
}

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
                ){
    
    // Table printing logic here

    printf("\nFinances Table:\n==========================================\t\t|\n");

    printf("Funds Available\t\t\t\t\t\t|\n");
    printf("------------------------------------------\t\t|\n");
    printf("\t%-32s | $ %-8.2f\t|\n", "Funds at Start of Semester", savings);
    printf("\t%-32s | $ %-8.2f\t|\n", "Grants/Loans/Scholarships/Gifts", grants);
    printf("\t%-32s | $ %-8.2f\t|\n", "Total After Tuition and Books", net_funds);
    printf("\t%-32s | $ %-8.2f\t|\n", "Funds Per Month", monthly_funds);  
    printf("\t%-32s | $ %-8.2f\t|\n", "Income Per Month", monthly_income); 
    printf("\t%-32s | $ %-8.2f\t|\n", "Total Per Month", monthly_budget);

    printf("------------------------------------------\t\t|\n");
    printf("Expenses Required (Per Month)\t\t\t\t|\n");
  
    printf("------------------------------------------\t\t|\n");
    printf("\t%-32s | $ %-8.2f\t|\n", "Rent", rent);
    printf("\t%-32s | $ %-8.2f\t|\n", "Utilities", utilities);
    for(int i = 0; i < num_expenses; i++){
        printf("\t%-32s | $ %-8.2f\t|\n", expense_names[i], expense_costs[i]);
    }

    printf("------------------------------------------\t\t|\n");
    
    printf("%-40s | $ %-8.2f\t|\n", "Essentials Total", essentials_total);
    printf("%-40s | $ %-8.2f\t|\n", "Essentials Available", monthly_essentials_avail); // change to pass 
    printf("%-40s | $ %-8.2f\t|\n", "Remainder", expensesRemaining); // change to pass
    printf("------------------------------------------\t\t|\n");

    printf("%-40s | $ %-8.2f\t|\n", "Available for Wants", monthly_wants);
    printf("------------------------------------------\t\t|\n");

    printf("%-40s | $ %-8.2f\t|\n", "Available for Savings", monthly_savings);
    printf("==========================================\t\t|\n");

}

void printSummary(float expensesRemaining, float wantsAvailable, float savingsAvailable) {
    

    float wants = fmax(0,wantsAvailable);
    float savings_monthly = fmax(0,savingsAvailable);

    if (expensesRemaining < 0.0) {
        printf("\nYou need to earn an additional $ %.2lf per month to pay your expenses and follow your budget rule.\n", -expensesRemaining*2);
    } 
    else {
        printf("\nYou are following your budget rule perfectly. The extra $ %.2lf per month can be set aside for a rainy day!\n", expensesRemaining);
    }
    printf("\nYou have $ %.2lf per month to spend on \"Wants\" such as video games, nights out, hobbies, etc.\n", wants);
    printf("\nOver 1 year, you will set aside $ %.2lf for savings.\n", savings_monthly * 12.0);
    printf("In 5 years with 5%% interest, your $ %.2lf will have grown to $ %.2lf.\n", savings_monthly * 12.0, savings_monthly * 12.0 * pow(1.05, 5));
    printf("In 10 years with 5%% interest, your $ %.2lf will have grown to $ %.2lf.\n", savings_monthly * 12.0, savings_monthly * 12.0 * pow(1.05, 10));
    printf("In 25 years with 5%% interest, your $ %.2lf will have grown to $ %.2lf.\n", savings_monthly * 12.0, savings_monthly * 12.0 * pow(1.05, 25));

}
