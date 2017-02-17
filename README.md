# WalkSafe

  WalkSafe allows users to alert their friends and family with a preset timer. The user can
  select the contacts they want to alert should their safety timer runs out. In order to stop the timer,
  the user must ping safe. If their timer runs out, the app will send an automated text messages to
  those contacts with the location of the user’s phone. To prevent this, the user must be aware of
  his or her timer and extend their timer accordingly to prevent false alarms. This forces the user to
  be aware of their surroundings and their app. However, should the user accidentally let their
  timer run out, a false alarm option will be prompted which allows the user to automatically send
  a follow up “false alarm” text message to their contacts.

## App Summary:
  * User selects contacts they would want to alert should timer run out.
  * User starts timer and must keep extending it if they do not arrive to safety within end of
  timer.
  * If timer hits 0, app will send automated text message to selected contacts from earlier
  with GPS location.
  * If user is safe at time of alert, user can then follow up with false alarm text message.
  Additional Features:
  * AlertDialog will be prompted if the app detects the user’s GPS is off during app run. It
  will then give the user the option to turn on their location and redirect the user to their
  phone’s location settings.
  * A “refresh location” button that will refresh the GPS location manually should the user
  want to.
  * A “PANIC” button that will automatically send a panic message to ALL contacts with 
  GPS location. EMERGENCY USE ONLY (Unless you want to get a lot of concerned text messages/calls)
  * A pin drop button that will open Google Maps to the current GPS location if the user
  wants to know where they are on a map.
  * App will toast a message to user to select at least one contact in case no contacts are
  selected.
  * App will send notification to user if timer runs out.
