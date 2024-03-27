/************************** 1170276A3.c **************************
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

The main for the CIS1500 Student Finance Planner. The main contains an array of Finance Data structs which is used to store various Finance data, as well 3 integer variables for budgeting ratios which can be changed by the user.

The main also contains an infinite while loop which prompts the user for input based on a given menu, and uses functions from A3_Functions.c to manipulate and store Financial Data. The while loop/program can be exited by entering anything other than 1 2 3 4 or 5

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

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "A3_Functions.h"

int main() {
    printf("Welcome to the CIS1500 Student Finance Planner. We have a few questions for you:\n\n");

    // Variable to store user's input
    int input = 0;

    // FinanceData struct array for storing finance data
    FinanceData fds[MAX_FINANCE_RECORDS];
    int numRecords = 0;

    // Variables for storing budgeting ratios (50/30/20 by default)
    int essentials_ratio = 50;
    int wants_ratio = 30;
    int savings_ratio = 20;

    // Infinite while loop. Program/loop is exited when the user inputs "6"
    while(1){
        // Main Menu
        printf("Enter a number to select from the options below:\n");
        printf("\t1) Read finances from file.\n");
        printf("\t2) Enter finances and save to file.\n");
        printf("\t3) Enter budget rules (ex. 50/30/20).\n");
        printf("\t4) Print most recent finances.\n");
        printf("\t5) Select finances to print.\n");
        printf("\t6) Exit.\n");

        fscanf(stdin, "%d", &input); // Gets user input

        // These if / if else statements execute the necessary processes for each menu option
        if(input==1){ // Input for reading Finance Data from file
          
          char filename[MAX_STRING_LEN]; // String variable for storing filename
          FinanceData fd; // FinanceData struct variable to store data from file

          // Prompts user for filename and stores their input in filename
          printf("Enter filename: "); 
          fscanf(stdin, "%s", filename);

          // Calls readFinanceDataFromFile with the filename entered by the user
          // and stores the data in the fields of fd
          fd = readFinanceDataFromFile(filename);

          // Checks if the Finance Data struct is valid and 
          // if there is room in the array of stored Finance Data.
          // If the struct is valid and there is room, it is added to the end of the array
          if(!fd.isValid){
            printf("Error: File not found\n");
          }
          else if(numRecords>=MAX_FINANCE_RECORDS){
            printf("Error: Maximum number of finance records already stored\n");
          }
          else{
            numRecords++;
            fds[numRecords-1]=fd;
          }
          
        }
        else if(input==2){ // Input for asking Financial Data from user and storing in a file

          char filename[MAX_STRING_LEN]; // String variable for storing filename
          FinanceData fd; // FinanceData struct variable to store data from file
          
          // Prompts user for filename and stores their input in filename
          printf("Enter filename: ");
          fscanf(stdin, "%s", filename);

          // Calls readFinanceDataFromUser which prompts the user for all necessary data and then stores it in Finance Data fd
          fd = readFinanceDataFromUser();

          // Calls saveFinanceRecordToFile which writes the Finance Data input by the user to a new file with the given file name
          saveFinanceRecordToFile(fd, filename);

          // Checks if the Finance Data struct is valid and 
          // if there is room in the array of stored Finance Data.
          // If the struct is valid and there is room, it is added to the end of the array
          if(!fd.isValid){
             printf("An error occured\n"); 
          }
          else if(numRecords>=MAX_FINANCE_RECORDS){
            printf("Error: Maximum number of finance records already stored\n");
          }
          else{
            numRecords++;
            fds[numRecords-1]=fd;
          }
          
        }
        else if(input==3){ // Input for changing budgeting rules, set to 50/30/20 by default
          
          // Calls getBudgetRules and updates the budget ratios based on the user's input
          getBudgetRules(&essentials_ratio, &wants_ratio, &savings_ratio);
          
        }
        else if(input==4){ // Input for selecting most recent finance data

          // Checks if there are any Finance Data structs stored, 
          // if there isn't the user is told to read data first
          if(numRecords==0){
            printf("No finance data loaded. Please read from a file first.\n");
          }
          else{
            // Prints the most recent Finance Data if there is struct(s) in the array
            printFinances(fds[numRecords-1], essentials_ratio, wants_ratio, savings_ratio);
          }
          
        }
        else if(input==5){ // Input for selecting specific finance data

          // Checks if there are any Finance Data structs stored, 
          // if there isn't the user is told to read data first
          if(numRecords==0){
            printf("No finance data loaded. Please read from a file first.\n");
          }
          else{
            // Calls selectFinanceData which prints a numbered list of stored Finance Data
            // and prompts user to enter an index which is stored in i
            int i = selectFinanceData(fds, numRecords);
            // Prints the Finance Data selected by the user
            printFinances(fds[i], essentials_ratio, wants_ratio, savings_ratio);
          }
          
        }
        else{ // if anything other than 1, 2, 3, 4, or 5 is entered by the user the program is exited
          printf("Exiting program.\n");
          exit(0);
        }
    }

   

    return 0;    
}