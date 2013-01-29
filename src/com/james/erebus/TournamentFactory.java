package com.james.erebus;

public abstract class TournamentFactory {
	
	public static Tournament createTournamentFromJson(String[] tournamentFields)
	{
		if(tournamentFields.length != 9)
		{
			throw new IllegalStateException("Incorrect number of tournament fields in array from Json object");
		}
		else
		{
			return new Tournament(tournamentFields[0], tournamentFields[1], tournamentFields[2], 
					tournamentFields[3], tournamentFields[4], tournamentFields[5], tournamentFields[6], 
					tournamentFields[7], tournamentFields[8]);
		}
	}

}
