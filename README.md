# Directory

This project uses a simple RestAPI to retrieve a list of users and display them in a "Directory". Each
entry can be expanded to show more information, and each user can also be displayed on a map given
the coordinates provided through the RestAPI. A `secrets.xml` file with a `<string name="maps_api_key">`
reference to a Google Maps SDK API key is needed to compile and deploy this app.

This was my first try at using Jetpack Compose to build a project and UI. Like anything in software, I can 
see the advantages and disadvantages to this style, but it's definitely interesting even if somewhat
immature in it's support throughout the Android ecosystem (testing).
