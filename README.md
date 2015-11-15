Star Realms Simulator
=====================

##Create a bot

Currently there is only one type of bot - *JsonBot.java*.

Other types of bots can be created by extending *Bot.java*.

See [Create JSON Bot](create_json_bot.md) for instructions on how to create a JSON bot

Once you have created your bot, add it to the *starrealmssimulator/bots* package and add a reference to it in *GameService.java*

##Running the Simulator

* You will need to have Java 8.


* In the *main* method of *GameService.java* you can either simulate one bot against a list of bots using *simulateOneAgainstAllBots* or you can simulate just two bots against each other using *simulateTwoBots*.


* Run the *main* method in *GameService.java* and the results will be printed to System.out.

##Results

See [Simulator Results](simulator_results.txt) for some interesting results from running the simulator.

