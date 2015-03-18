# FOODit Test

Create the set of REST API's that can answer the following questions:

  1.	Total number of orders for each restaurant
  2.	Total amount (money) of sales per restaurant
  3.	The most frequently ordered meals on FOODit Platform
  4.	The most frequently ordered category for each restaurant

It is expected that you will write unit tests.

Deploy the solution to your own appengine instance and provide FOODit with links to the API's in a email so we can
test it.

Your data to run the API's should be stored in Google Datastore


## Pre-requisites.
The solution is to be built on the thundr framework and deployed to google appengine.
You can read more about thundr here http://3wks.github.io/thundr/.

To simplify environment setup and allow you to jump straight into solving the test questions this project is has all the dependencies for thundr and appengine.

## Get started.
1. Clone this repository https://github.com/FOODit/FOODit-JavaTest.git

2. Run mvn install to install the application

3. Run mvn appengine:devserver to start the application locally.

4. Start writing the code to expose the data required in the test.

5. When your happy with your solution you will need to deploy it to your own appengine instance and
test that it works. Please also push the solution to your own public github repository so we can review the code

When your ready please send us the urls of the created api's and the link to the projects github repository.

## Tips
To expose the api's you can simple return a JsonView from your controller method read about views here
http://3wks.github.io/thundr/thundr/views.html

Happy coding :)

# Solution docs

## API

`GET /reports/orders`: Get total number of orders for all the restaurants in the system

`GET /reports/orders/{restaurant}`: Get total number of orders for a restaurant

`GET /reports/sales`: Get total value of orders for all the restaurants in the system

`GET /reports/sales/{restaurant}`: Get total value of orders for a restaurant

`GET /reports/topmeals`: Top 10 most popular meals platform-wise

`GET /reports/topcategories`: Most popular categories for each restaurant in the system

`GET /reports/topcategories/{restaurant}`: Most popular categories for a restaurant

## AppEngine
http://foodit-java-test.appspot.com/
I did not manage to get it running on app-engine
I have tried using the following tools:
- command line tool (appengine sdk)
- maven plugin
- eclipse plugin
