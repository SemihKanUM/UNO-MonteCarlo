# Project-2.1-UNO
To run our code you need to navigate through test/desktop/src and run DesktopLauncher

Before running the code:
First of all (IMPORTANT): If you are playing on a macOS make sure to get your screen details (width and height) of your laptop and input it on line 17 on one of the or statements. For example on windows the default settings are either width=1920 and height=1080 OR width=2048 and height=1536

After inputting your screen resolution you can run the file that will initialize the game. To start playing you should press "start game" button. After that input the number of real players and AI competitors based on Monte Carlo Tree algorithm, that you want to play with, at a maximum of 10 players. AI players that use machine learning will be implemented in next phase. Subsequently, you can press "continue" and then press space to start the game. 

A player can only play one card at a time, so after a player has played his card he should click the button to go to the next player which is at the bottom left of the screen. Do not forget to click the uno before playing the second card as that will result in a penalty, and therefore you get +2 cards.

Have fun while playing the game!

Testing:Extract the files from Logic-Testing.zip and open it as a gradle project to run the JUnit tests
The game ends when one player uses theirs last card. At that moment, the scoreboard opens and shows points gained by all the players.
