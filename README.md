ChessProblem
============

The program solves multiple unguard chess pieces arrangements problem.

Dependencies: JRE 1.8

### How to run

In Unix like OS use chess-problem.sh script, in Windows chess-problem.bat.
Being run with no arguments script shows usage:

```shell
usage: chess-problem [-bh <arg>] [-bw <arg>] [-ps <arg>]
Solves multiple unguard arrangements problem.
 -bh,--board-height <arg>   Board height: number in range 1-127
 -bw,--board-width <arg>    Board width: number in range 1-127
 -ps,--pieces-set <arg>     Pieces set in the following format: "K1-Q1-R1-B2-N3". Where character signifies the piece
                            type and number is a number of pieces of this type. In the example we have: K1 - one king,
                            Q1 - one queen, R1 - one rook, B2 - two bishops and N3 - three knights.
```

### Example

```shell
$ ./chess-problem.sh -bh 8 -bw 4 -ps K1-Q1-R1-B2-N3
04:48:47.743 [main] INFO  c.Solver - Start solving: board 4x8, pieces = [King, Queen, Rook, Bishop, Bishop, Knight, Knight, Knight]
04:48:47.992 [main] INFO  c.Solver - Operation took 0 sec. Found 9012 solutions. Run 91121 loops.

```






