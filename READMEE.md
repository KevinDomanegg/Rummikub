# Rummikub
Implementation of the board game "Rummikub" using JavaFX.


--- Rules ---

The goal of the game is to get rid of all the stones on your hand.
At every turn you can either put stones from your hand on the table or you have to pick up a stone from the bag.

You can put down stones only in the form of one of two types of groups:
- Group: 3 or more stones with the same numerical value
- Run:  3 or more stones with the same color and andjacent numetical values

or you can add stones from your hand to groups already existing on the table.

In order to put down stones for the first time, the numerical values of cards you are trying to put down must add up to 40 or more.

You can move stones freely on the table in order to create opportunities for adding stones from your hand.
However, when you are confirming your move, the table must be consistent, i.e. every stone on the table must be part of a valid group.

The first player to run out of stones on his hand wins the game.
The other players get ordered by the added values of their cards (lower is better).


--- Controlls ---

To connect to a host you will need to enter his IP-address.

You can move stones by simpling dragging and dropping them.
You can move groups of cards with ctrl + drag'n'drop.

Additional info is provided in-game under the 'help'-section.


--- Info ---

This project is the product of the work of six highly engaged students over the course of five weeks. I did not make this alone.

Enjoy!
