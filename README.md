# Kitties

## How to build
* Clone the project 
* Add to `local.properties` your [TheCatApi](https://documenter.getpostman.com/view/5578104/RWgqUxxh)'s key like this:
```
KITTIES_KEY=YOUR_API_KEY
```

## Branches
* without-paging: closer to completion
* with-paging-doesnt-work: want-to-have feature, will be implemented after the bug is fixed. The bug is: if the list is invalidated (e.g. filters are chosen), the list is stuck in invalidation cycle. The discussion about possible solution is [here](https://kotlinlang.slack.com/archives/C0B8M7BUY/p1626218957212300).

## Known issues
* UI/UX is far from perfect, there is no loading state indicators or empty list disclaimers
* No unit tests 
* There is no pagination for the list in `without-paging`, and it partly doesn't work in `with-paging-doesnt-work`
![device-2023-02-04-125258](https://user-images.githubusercontent.com/10089784/216763395-b8d7d835-ec9f-4179-aed3-8b25e6db80c6.gif)
