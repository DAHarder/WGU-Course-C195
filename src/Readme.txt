Scheduling Application

Dan Adams
Dada141@wgu.edu

Application ver 1.0
11/12/2020

INTELLIJ IDEA Community 2020.2
JavaFX 11.0.2
JDK 11.0.8

PURPOSE OF APPLICATION:
This application is to be used per contract with the Consulting organization 'X' and is to be used as a scheduling application
for scheduling, tracking, and reporting on varying appointments with use for the relationship of the companies Contact employees to Customers.


ASSESSOR NOTES:
    Thank you for taking the time to assess this project.  Hopefully the below can help a bit.

RUBRIC NOTES:

A:GUI-BASED APPLICATION
    See application

A1:LOG-IN FORM
    See LoginController
        Username/Password - test/test
        English/French/Japanese used as automatic translation
        Error message displays under login field

A2:CUSTOMER RECORD FUNCTIONALITIES
    See CustomersController
        *notes
            Select a customer record from the table, from you you can delete the record, or edit the fields and save.
                For the customer deletion - I am assuming the initial alert popup and refreshing the table is enough to fulfill the custom message requirement
            Press new customer button to add a new customer.
            Press back to go back to the mainscreen.


A3A:SCHEDULING FUNCTIONALITY: ADD, UPDATE, AND DELETE
    See AppointmentAddController and AppointmentModifyController
        Can be accessed from Add Appointment button on mainscreen and Edit button after selecting a table item
        Deletion can be done by selecting a table item in the Mainscreen and clicking the delete button
            Custom message displayed in Alert

A3B:SCHEDULING FUNCTIONALITY: VIEWS
    See ALL, WEEK, MONTH, NEXT, and PREVIOUS buttons on the mainscreen

A3C:TIME ZONES
    See data in database and in the tableview

A3D:INPUT VALIDATION AND LOGICAL ERROR CHECKS
    See Edit Customer and Add Appointment buttons
        All input validations should be in place with alert messages and the fields that fail validation should highlight red.

A3E:ALERTS
    See MainScreen Controller - Initialization method
        Line 552
        Alerts should display if there is an alert 15 minutes before and after current time, and anything else should say no appointments.

A3F:REPORTS
    See Mainscreen Controller
        Appointments by type and month:  If you click the 'month' button, it should show the total appointments by type on the bottom of the screen
        Contact schedules: If you use the Contact filter combobox on the left, it should filter the appointments by that contact and show the count on the bottom of the screen
        Additional report:  On the bottom of the screen it should show the amount of 'unique customers'

B:LAMBDA EXPRESSIONS
    See Login Controller
        Line 89
    See AppointmentAdd Controller
        Line 232

C:TRACK USER ACTIVITY
    See login_attempt.txt under SRC folder
    See LoginController
        line 112, 125, 144

D:JAVADOC COMMENTS
    See JavaDoc folder in root

E:README.TXT
    See this file in root

