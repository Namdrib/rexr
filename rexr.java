import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class rexr
{
	public static void usage(String programName)
	{
		System.out.println("Usage:");
		System.out.println("Add an exercise entry to a user");
		System.out.println(
				"java " + programName + " add name [pushups situps squats]");
		System.out.println("  To add a set of exercises towards name");
		System.out.println(
				"  If {pushups,situps,squats} is not given, at least three numbers\n  must be provided from stdin, otherwise the program will exit with no outcome");
		System.out.println("--");
		
		System.out.println("Summarise user data");
		System.out.println("java " + programName + " summarise [name]");
		System.out.println(
				"  To produce a human-redable summary of name's exercise so far,");
		System.out.println("  or leave the name out to summarise all");
		System.out.println("--");
		
		System.out.println("List all users with existing entries");
		System.out.println("java rexr list");
		System.out.println("  To list the names of all users who have added at least one entry");
		System.out.println("--");
		
		System.out.println("Read docs/readme.md for more information");
	}

	// Returns whether a string is a number
	private static boolean isNum(String s)
	{
		try
		{
			Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		return true;
	}

	// Return a List<Integer> from the user
	// If args has enough integers in it, the user that, otherwise
	// take the numbers from the user from System.in
	private static List<Integer> getNumbersFromUser(String[] args,
			int firstNonOption)
	{
		int numCorrectArgs = 0;
		for (int i = firstNonOption; i < args.length; i++)
		{
			if (isNum(args[i]))
			{
				numCorrectArgs++;
			}
			else
			{
				break;
			}
		}

		List<Integer> out = new ArrayList<Integer>();
		// Take the numbers from the arguments
		if (numCorrectArgs >= 3)
		{
			for (int i = firstNonOption; i < numCorrectArgs
					+ firstNonOption; i++)
			{
				out.add(Integer.parseInt(args[i]));
			}
			return out;
		}
		else // Take numbers from user input
		{
			Scanner scanner = new Scanner(System.in);
			while (true)
			{
				String temp = scanner.nextLine();
				temp = temp.trim();
				if (temp.isEmpty() || temp.equals("") || !isNum(temp))
				{
					break;
				}
				else
				{
					out.add(Integer.parseInt(temp));
				}
			}
			scanner.close();
		}

		return out;
	}

	public static boolean matchesSummary(String s)
	{
		return s.matches("summar(y|i(s|z)e)");
	}

	public static void main(String[] args)
	{
		String programName = "rexr";
		if (args.length < 1)
		{
			usage(programName);
			return; // Calling exit will probably quit the bot
		}

		String command = args[0];
		Recorder r = new Recorder();
		switch (command)
		{
			case "add":
			{
				if (args.length < 2)
				{
					usage(programName);
					return;
				}
	
				String user = args[1];
				List<Integer> array = getNumbersFromUser(args, 2);
				try
				{
					if (r.addEntryFor(user, array))
					{
						System.out.println("Succesfully added entry to " + user);
					}
					else
					{
						System.out.println("Failed to add entry to " + user);
					}
				}
				catch (Exception e)
				{
					System.out.println(e);
				}
				break;
			}
			case "summarise":
			{
				// Check for another command
				if (args.length >= 2)
				{
					// for each argument
					// attempt to consolidate that user
					// if that files does not exist, stop
					for (int i = 1; i < args.length; i++)
					{
						String user = args[i];
						try
						{
							r.summarise(user);
							if (i < args.length - 1)
							{
								System.out.println("");
							}
						}
						catch (IOException e)
						{
							e.printStackTrace();
							System.out.println("Error, could not summarise " + user);
							break;
						}
					}
				}
				else
				{
					// summarise all "docs/*_exercise.log" into "docs/summary.log"
					try
					{
						r.summarise("");
					}
					catch (IOException e)
					{
						e.printStackTrace();
						System.out.println("Error, could not summarise all users");
					}
				}
				break;
			}
		
			case "list":
			{
				Set<String> users = r.getListOfUsers();
				for (Iterator<String> it = users.iterator(); it.hasNext();)
				{
					System.out.println(it.next());
				}
				break;
			}
			
			default:
			{
				usage(programName);
			}
		}
	}
}
