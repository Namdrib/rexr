
// A bunch of file-writing/reading imports
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Recorder
{
	private final int		numDefaultExercises	= 3;
	// private String[] names = { "push-ups", "sit-ups", "squats", "pull-ups"};
	private final String	filesLocation		= "data/";
	private final String	exerciseFileSuffix	= "_exercise.log";
	private final String	summaryFileSuffix	= "_summary.log";
	private final String	entryDelim			= " ";

	Recorder()
	{
		;
	}

	private String getExerciseFileLocation(String user)
	{
		return filesLocation + user + exerciseFileSuffix;
	}

	private String getSummaryFileLocation(String user)
	{
		if (user.isEmpty())
		{
			return filesLocation + summaryFileSuffix.substring(1);
		}
		else
		{
			return filesLocation + user + summaryFileSuffix;
		}
	}

	// Force value to be a value between min and max
	private static int clamp(int value, int min, int max)
	{
		return Math.max(min, Math.min(max, value));
	}

	// Read through filename line-by-line
	// and compare against query
	// Return true iff filename contains a line the same as query
	private boolean fileHasLine(String filename, String query)
			throws IOException
	{
		try (FileReader fr = new FileReader(filename);
				BufferedReader br = new BufferedReader(fr))
		{
			for (String s; (s = br.readLine()) != null;)
			{
				if (s.trim().equals(query))
				{
					return true;
				}
			}
			return false;
		}
		catch (IOException e)
		{
			throw e;
		}
	}

	// Return true iff the first line is the only line, and it is null
	private boolean fileEmpty(String filename) throws IOException
	{
		try (BufferedReader br = new BufferedReader(new FileReader(filename)))
		{
			return br.readLine() == null;
		}
	}

	// Append the listed exercises into a file for the user
	public boolean appendTo(String user, List<Integer> exercises)
			throws IOException
	{
		// Ensure appropriate size
		if (exercises.size() < numDefaultExercises)
		{
			return false;
		}

		// Build the output string based on exercises
		String out = new String();
		for (int i = 0; i < exercises.size(); i++)
		{
			int n = clamp(exercises.get(i), 0, 999);
			out += String.format("%03d", n);
			if (i < exercises.size() - 1)
			{
				out += entryDelim;
			}
		}
		System.out.println("Adding to " + user + ": " + out);

		// If user file DNE, create it
		// Otherwise write in append mode
		// Loop through exercises and parse that into an appropriate string
		String filename = filesLocation + user + exerciseFileSuffix;
		try (FileWriter fw = new FileWriter(filename, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter pw = new PrintWriter(bw))
		{
			// Add a date timestamp if necessary to mark a new day
			// If this is the first entry in this file,
			// do not add extra new line to the beginning
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String formattedDate = "--- " + sdf.format(new Date()) + " ---";
			if (!fileHasLine(filename, formattedDate))
			{
				String dateOut = formattedDate + "\n";
				if (!fileEmpty(filename))
				{
					dateOut = "\n" + dateOut;
				}
				pw.append(dateOut);
			}

			pw.append(out + "\n");
		}
		catch (IOException e)
		{
			throw e;
		}

		return true;
	}

	// Summarise the user's log such that there exists one entry per day
	// describing the total exercise logged for that day
	// If no user is provided, summarise ALL users
	// Ignore extraneous exercises (only do push-ups, sit-ups, and squats)
	public boolean summarise(String user) throws IOException
	{
		if (user == null || user.isEmpty()) // Summarise all
		{
			// TODO : this will be large...
			// open all files data/*_exercise.log
			// read through all of them to determine which date was earliest
			// do this date on all files that have this date
			// those which don't should have a `-` entry instead of numbers
			// ideally pretty print so that each row is a data, each column is from a file
			
			// Perhaps it'll be easier to do this if individually
			// summarise each thing first, then append?
		}
		else // Summarise just for user, if the user exercise file exists
		{
			try (FileReader fr = new FileReader(getExerciseFileLocation(user));
					BufferedReader br = new BufferedReader(fr);
					FileWriter fw = new FileWriter(
							getSummaryFileLocation(user));
					BufferedWriter bw = new BufferedWriter(fw))
			{
				// Write headers
				bw.write("DATE       | " + user + "\n");
				bw.write("-----------+-------------\n");

				// Read from br day-by-day, accumulating the {1,2,3}
				// and write the day summary into bw
				for (String s; (s = br.readLine()) != null;)
				{
					String date = new String();
					if (s.startsWith("---"))
					{
						date = s.substring(4, s.length() - 4);
					}
					int[] total = { 0, 0, 0 };

					// Read all entries for this date
					while (true)
					{
						s = br.readLine();
						if (s == null || s.trim().isEmpty())
						{
							break;
						}

						String[] parts = s.split(" ");
						for (int i = 0; i < total.length; i++)
						{
							if (i < parts.length)
							{
								total[i] += Integer.parseInt(parts[i]);
							}
						}
					}

					// Write the totalled sum for this date
					String out = new String();
					for (int i = 0; i < total.length; i++)
					{
						out += String.format("%03d", total[i]);
						if (i < total.length - 1)
						{
							out += " ";
						}
					}
					bw.write(date + " | " + out + "\n");
				}
			}
			catch (IOException e)
			{
				throw e;
			}
		}
		return true;
	}
}
