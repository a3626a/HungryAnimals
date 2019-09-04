# Home

Hungry-Animals focuses on providing variety of configurations. Therefore, its configuration system is necessarily complicated. This article will help you to figure out mysterious files and let you configure details of Hungry-Animals.

## What Can Hungry-Animals Do?

* Chicken Eating Ocelots
* Lava Producing Cows
* Diamond Laying Chickens
* Defensive Pigs
* Modded Animals

## Easy Config

Hungry-Animals provides easy config. Users can set the difficulty and tempo simply.

Go to `config/hungryanimals_example/master/master.json`. If you don't have this file, just launch your Hungry-Animals installed Minecraft once.

```text
{
    "difficulty" : "normal",
    "tempo" : "normal"
}
```

This file is inside "example folder", so editing it won't affect Minecraft. Copy this file into `config/hungryanimals/master/master.json`

`"difficulty"` can be one of `"easy"`, `"normal"`, and `"hard"`. Animals in `"easy"` will eat less food.

`"tempo"` can be one of `"fast"`, `"normal"`, and `"slow"`. Animals in `"fast"` will grow and create babies faster. Animals eat same amount of food regardless of `"tempo"`, so they will drop less meat in `"fast"`. In other words, wheat to meat conversion rate is constant.

## Detailed Config And Examples

`'config/hungryanimals_example'` folder does not affect Minecraft gameplay. All files under this directory are examples for server admins and modpack creators.

By default, there are no files under `'config/hungryanimals'`, there are only directories. Then, all missing configurations are loaded from internals of Hungry Animals.\(which are the same files as under example folder\) If you want to adjust json files, just save json file under `'config/hungryanimals'`, this changes will be applied to Hungry Animals.

