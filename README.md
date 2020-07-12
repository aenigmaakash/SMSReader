SMS Analyser for Transactional Messages in Kotlin
-------------------------------------------------------
Used 3rd party content provider to get sms data from the phone
'me.everything:providers-android'

Then using pattern matching regex found online, extracted transactional smss' from the list
Extracted bank account, amount debited or credited, and time from the messages

Added all the data to a custom adapter that shows the list in a listView

Using the data found, made a pie chart using 'hello-charts' library
com.github.lecho:hellocharts-library

###########################################################

To use
--------
-Start the app and click analyse sms, allow permission
-Details of sms will be populated below in a listView
-Tap on any message item to add tag or delete that message
-Click on View Charts to view pie chart
