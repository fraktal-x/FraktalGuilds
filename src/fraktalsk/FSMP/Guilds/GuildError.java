package fraktalsk.FSMP.Guilds;

public enum GuildError {
	NotPlayer,
	CannotPerformActionOnSelf,
	
	TargetNotInGuild,
	TargetAlreadyInGuild,
	
	CannotPromote,
	CannotDemote,
	CannotLeave,
	
	NotInGuild,
	AlreadyInGuild,
	
	NoPermission,
	
	NoPlayerWithNameExists,
	NoGuildWithNameExists,
	NoNameExists,
	
	NoGuildsExist,
	
	GuildAlreadyExistsWithName,
	GuildCannotEqualPlayerName,
	
	RallyCooldown,
	RallyExpired,
	RallyOwnerNotFound,
	
	NoInviteFound,
	InviteExpired,
	GuildNoLongerExists,
	
	IncorrectArgs,
	
	IncorrectUsageColour,
	IncorrectUsageCreate,
	IncorrectUsageDelete,
	IncorrectUsageDemote,
	IncorrectUsageInfo,
	IncorrectUsageInvite,
	IncorrectUsageKick,
	IncorrectUsagePromote,
	IncorrectUsageRename,
	IncorrectUsageResign,
	IncorrectUsageTell
}
