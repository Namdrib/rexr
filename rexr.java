import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class rexr
{
	public static void usage(String programName)
	{
		System.out.println("Usage:");
		System.out.println("java " + programName
				+ "user name [numPushups numSitups numSquats]");
		System.out.println("  To add a set of exercises towards name");
		System.out.println(
				"  If num{Pushups,Situps,Squats} is not given, at least three");
		System.out.println(
				"  numbers must be provided from stdin, otherwise the program");
		System.out.println("  will exit with no outcome");
		System.out.println("OR\n");
		System.out.println("java " + programName + " summar(y|i(s|z)e) [name]");
		System.out.println(
				"  To produce a human-redable summary of name's exercise so far,");
		System.out.println("  or leave the name out to summarise all\n");
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
			for (int i = firstNonOption; i < numCorrectArgs + firstNonOption; i++)
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
		if (command.equals("user"))
		{
			if (args.length < 2)
			{
				usage(programName);
				return;
			}

			String user = args[1];
			// int[] array = {-9999, -1, 0, 1, 12, 123, 1234};
			List<Integer> array = getNumbersFromUser(args, 2);
			try
			{
				if (r.appendTo(user, array))
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
		}
		else if (matchesSummary(command))
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
					}
					catch (IOException e)
					{
						e.printStackTrace();
						System.out
								.println("Error, could not summarise " + user);
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

		}
		else
		{
			usage(programName);
		}
	}
}
