# Blue Screen of Death, on Death
BSODOD is a Forge mod that intends to make Minecraft a bit more challenging.
It does so by making you blue screen (i.e. making your entire computer crash) when you die in the game.

## How does it work? Is it risky?

The mod uses undocumented [Native API](https://en.wikipedia.org/wiki/Native_API) methods to raise an error in a critical system process and instructs the system to shutdown on said error, meaning blue screen. It does not damage your system to create the blue screen.

  

That being said,
#### SAVE ANY UNSAVED WORK ON YOUR COMPUTER BEFORE PLAYING WITH THIS MOD.

## I blue screen'd when I shouldn't have!
I do my best to prevent that from happening, however if you believe you have encountered a bug in the mod, feel free to [open an issue](https://github.com/Cosmiiko/MC-BSODOnDeath/issues/new/choose). Try to be as descriptive about it as possible. What exactly happened in your game the seconds before blue screening? Retrace your steps.

## Mod Configuration  
You can configure the mod in `.minecraft/config/bsodod-client.toml`.  

##### openUrl
The URL that opens before you blue screen. Rick Astley's Never Gonna Give You Up by default. Set to `'none'` to disable.  

##### delayInSecs
The delay in seconds before you blue screen after your death.  

##### defused
Whether or not the mod is defused. If true, the blue screen will be replaced by a log to the console.
