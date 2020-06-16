# Fatashi -- programu yenye kupenda kuchunguachungua sana kamusi mbalimbali kierevu

Fatashi is a smart dictionary lookup app for Android devices. 

Main features: 

* robust regex ability to look up multiple items at same time 
* smart kiswahili handling: automatically detects words matching certain types and adjusts regex to handle both types
  ** ma-noun -> (ma)?-noun
  ** vi-noun -> [kv]i-noun
  ** mi-noun -> mi?-noun
  ** noun-ni -> noun(ni)?
* ability to chain dictionaries; if lookup fails in one dictionary, proceeds down the chain until a match is found. 
* ability to have non-dictionary resources, such as proverbs, idioms databases
* database is simple text files with fields delimited by any consisten nonambiguous set of characters
* shorthand special symbols easily accessed from main android keyboard to guide/constrain searches


