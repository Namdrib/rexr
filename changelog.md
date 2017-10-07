# rexr changelog:
- Author - Denton Phosavanh
- For: Yes
- Latest version: 1.0.1

---

## Upcoming changes
- Allow adding of "REST" days
- Consolidate ALL users' files into a summary
- Discord Bot this
- Better command line parsing

## [1.0.1] - 2017-10-07
### Added
- `list` functionality - lists all users who are recorded

### Removed
- support for multiple aliases of `summarise`
	- "summary", "summarize" no longer work (Sorry, U.S. users)
	- as a consequence, the code that handles commands is now a switch/case instead of if/else

### Changed
- the `user` command is now the `add` command
	- this helps keep in check with the whole "everything is a verb" mentality
- some typos

## [1.0.0] - 2017-09-10
### Added
- reading name and cmd line arguments OR arguments from stdin
- correctly inputs storing into exercise.log file
- summarising a single user at a time (use of `rexr summary [names]`)

## [0.0.0] - 2017-09-09
### Added
- readme, changelog
- initial ideas and files
