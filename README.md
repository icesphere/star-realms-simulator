Star Realms Simulator
=====================

##Create a bot

Bots can be created by extending *Bot.java*.

See [Create JSON Bot](create_json_bot.md) for instructions on how to create a JSON bot

Once you have created your bot, add it to the *starrealmssimulator/bots* package and add a reference to it in *GameService.java*

##Running the Simulator

* You will need to have Java 8.


* In the *main* method of *GameService.java* you can either simulate one bot against a list of bots using *simulateOneAgainstAllBots* or you can simulate just two bots against each other using *simulateTwoBots* or you can simulate all bots against all bots using *simulateAllAgainstAll*.


* Run the *main* method in *GameService.java* and the results will be printed to System.out.

##Results

See [Simulator Results](simulator_results.txt) for some interesting results from running the simulator.

##Trello Board

If you are interested in seeing what is being worked on, or if you would like to help work on the simulator, check out the Trello Board: [https://trello.com/b/OYgPboK0/star-realms-simulator](https://trello.com/b/OYgPboK0/star-realms-simulator)
