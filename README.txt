Jayveer Singh - 603812809
Partner: Alejandro Veloz - 003796497

Tables
	
	
Item
	ItemID [int]
	Name [varchar]
	Buy Price [money int]
	First Bid Minimum [money int]
	Started [date]
	Ends [date]
	Description [varchar]
	Number of Bids [int] // Keep in favor of simpler database queries
	Current bid amount [money int] // Keep in favor of simpler database queries
	
	Key: ItemID
	Dependencies:
		ItemID -> Name, Buy Price, First Bid Minimum, Started, Ends, Description, Number of Bids, Current Bid ID

User
	UserID [varchar]
	Rating [int]
	Location [varchar]
	Country [varchar]
	
	Key: UserID
	Dependencies:
		UserID -> Rating, Location, Country

Category
	CategoryName [varchar]
	
	Key: CategoryName
	Dependencies:
		None
		
Bid
	ItemID [int]
	Time [date]
	UserID [varchar]
	Amount [money int]
	
	Key: ItemID, Time
	Dependencies:
		ItemID, Time -> UserID, Amount

ItemSeller
	ItemID [int]
	UserID [varchar]
	
	Key: ItemID
	Dependencies:
		ItemID -> UserID

ItemCategory
	ItemID [int]
	CategoryName [varchar]
	
	Key: ItemId, CategoryName
	Dependencies
		None

One thing we came across

