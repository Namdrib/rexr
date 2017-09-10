# rexr readme

## Purpose
- rexr, short for record-exercise, is a way to semi-automate the recording of exercise counts on a daily basis
	- Previously, it was up to the user to remember their exercise count for the day, and determine their own "bounds" for what constituted the current day.
	- This program will automatically perform day splitting and recording the exercise number in a database or file.
	
- At some point, make a Discord bot out of this, so a user can invoke the log associated with their unique Discord ID
	- e.g. `@discordBot num1 num2 num3` will add the exercises to the user's something
	- then using `@discordBot summarise` will summarise that user's entry
	- or using `@discordBot summarise all` will summarise everyone's thing
	
## Usage
### Requirements:
- `make` (optional)
- `java`

### Compilation
- `make` or `javac rexr.java`

### Running
#### To record individual sessions
- `java rexr user name [num1 num2 num3 [..]]`, where:
	- `user` represents that the user wants to add an entry to a user
	- `name` is a single word, containing no spaces. This is what identifies the current user. The name may NOT be "summarise" - this is a reserved command
	- `num{1,2,3..n}` represent the number of push-ups, sit-ups, and squats to record against `name` for the current day
		- if these are to be given, they MUST be given together (at least three numbers)
			- if at least three of these arguments are provided, the program will keep reading until the first non-integral entry.
			- that is: using `java rexr user name 1 1 1 1 q` will register the four 1s
		- if these are NOT given, then the user will be prompted to enter the numbers separately from stdin
			- if insufficient number of numbers is given in stdin, the program will exit with no outcome.
			- the user can signify finishing input by giving an extra [enter] key press

#### To create a summary list
- `java rexr summar(y|i(s|z)e) [name [names..]]`
	- This creates a file summarising the previous efforts (condensing all activities of a day into a single entry) in a human-readable manner
	- if `name` is provided, it will summarise a single file
	- otherwise, it will summarise ALL exercise files

## Other information
- the generated files will be stored in the `data` directory

### File format
#### Naming convention
- the names of the files will be of the format:
	- `name`\_exercise.log for the exercise log files
	- `name`\_summary.log for the _individual_ summary files
	- summary.log for the _total_ summary file

#### File contents
- the contents of the exercise log files will contain:

```
--- date ---
n1 n2 n3 ...
n4 n5 n6 ...

--- date+1 ---
...

```

- where: 
	- the date (chronological order) is given in YYYY/MM/DD format for easier sorting
	- each subsequent line represents the exercises done in a single session
	- a new line to separate days
	
- the contents of the summary files will contain:

```
DATE       | NAME1       | NAME2       | NAME3
-----------+-------------+-------------+------------
1970/01/01 | -           | 100 100 100 | 100 100 100
1970/01/02 | 010 001 000 | REST        | REST
1970/01/03 | 100 100 100 | 010 000 000 | 100 100 100
...
```

- where:
	- each date has its own row
	- each participant has their own column
	- evenly-spaced to make it easier to read
		- if I feel like it, putting nice borders between rows/columns
	- the number caps out at 999 to prevent dynamic spacing issues
	- a single `-` denotes that the participant had no entries for the given date
		- this is only used when summarising all users into a single file
	- the last column does not have extra spacing to pad it to the right
	- there will only be one name column if it is a named summary file
	- a row will exist if at least one participant had entries in there
	- not necessarily inclusive of EVERY date
		- so for example, if nobody did anything on 1970/01/04, but people did things on 1970/01/05, then the fourth of January will be omitted.
