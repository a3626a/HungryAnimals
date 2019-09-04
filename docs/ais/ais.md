# AIs

## Introduction

AI of an animal is defined by a AI container and AI modifiers. AI containers decide category of AI, like "herbivore" or "wolf". AI modifiers change details of AI container. AI modifiers are applicable to specific AI containers.

## Grammar

{% code-tabs %}
{% code-tabs-item title="ais/{modid}/{anima}l.json" %}
```text
{
  "type" : ai_container,
  ai_modifier,
  ai_modifier,
  ...
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="ai\_container " %}
```text
"herbivore" |
"rabbit" |
"wolf"
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="ai\_modifier " %}
```text
"attack_melee" : {
  "speed" : double,
  "use_long_memory" : boolean
}
|
"avoid_player" : {
  "radius" : double,
  "farspeed" : double,
  "nearspeed" : double
}
|
"mate" :{
  "speed" : double
}
|
"drink_milk" :{
  "speed" : double,
  "fluid" : string,
  "amount" : integer
}
|
"trough" : {
  "speed" : double
}
|
"tempt" : {
  "speed" : double,
  "scared_by" : boolean,
  "items" : ingredient | [ingredient, ...]
}
|
"tempt_edible" : {
  "speed" : double,
  "scared_by" : boolean
}
|
"eat_item" : {
  "speed" : double,
  "only_natural" : boolean
}
|
"eat_block" : {
  "speed" : double
}
|
"follow_parent" : {
  "speed" : double
}
|
"hurt_by_player" : {
  "call_help" : boolean
}
|
"hunt_non_tamed" : {
  "chance" : integer,
  "check_sight" : boolean,
  "only_nearby" : boolean,
  "herding" : boolean
}
|
"hunt" : {
  "chance" : integer,
  "check_sight" : boolean,
  "only_nearby" : boolean,
  "herding" : boolean
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

## Usage

There are 3 ai\_containers, `"herbivore"`, `"rabbit"`, and `"wolf"`. ai\_container removes Vanilla AIs, and determines possible ai\_modifiers. `"herbivore"` removes Vanilla AIs such like being tempted, mating, eating grass. `"rabbit"` is similar to `"herbivore"`. It removes Vanilla AIs specialized to rabbit. `"wolf"` removes Vanilla AIs mentioned above, and hunting.

| ai\_container | `"attack_melee"` | `"avoid_player"` | `"hurt_by_player"` | `"hunt_non_tamed"` | `"hunt"` | `"mate"` | `"drink_milk"` | `"trough"` | `"tempt"` | `"tempt_edible"` | `"eat_item"` | `"eat_block"` | `"follow_parent"` |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `"herbivore"` | O | O | O | X | X | O | O | O | O | O | O | O | O |
| `"rabbit"` | O | O | O | X | X | O | O | O | O | O | O | O | O |
| `"wolf"` | X | X | X | O | O | O | O | O | O | O | O | O | O |

Each ai\_modifier has parameters, to indicate details like speed, and conditions. Following lists are explanations of ai\_modifiers and their parameters.

`"attack_melee"` : The animal attacks target while moving at `"speed"`. This AI requires another targeting AI like `"hurt_by_player"`. If `"use_long_memory"` is true, the animals will keep attacking target even if the animals can not reach the target.

`"avoid_player"` : The animal flees from player when the animal is wild. it flees when distance to the player is less than `"radius"`. It moves at `"farspeed"` if distance to the player is more than 7m, at `"nearspeed"` otherwise.

`"mate"` : The animal mates with other animal while moving at `"speed"`.

`"drink_milk"` : The baby animal drinks `"fluid"` from any other animal which produces `"fluid"`. The baby animal movies at `"speed"`. `"amount"` decides how fast the baby drinks.

`"trough"` : The animal eats item inside trough while moving at `"speed"`.

`"tempt"` : The animal is tempted by `"items"` while moving at `"speed"`. If `"scared_by"` is true, the animal stop to be tempted when the player moves.

`"tempt_edible"` : The animal is tempted by edible items \(food preferences\) while moving at `"speed"`. If `"scared_by"` is true, the animal stop to be tempted when the player moves.

`"eat_item"` : The animal eats fallen items while moving at `"speed"`. If `"only_natural" : true`, the animal only eats items gathered by itself \(like hunting\).

`"eat_block"` : The animal eats blocks while moving at `"speed"`.

`"follow_parent"` : The animal follows its parent while moving at `"speed"`.

`"hurt_by_player"` : The animal targets the attacker \(player only\), this target is used by `"attack_melee"`. If `"call_help"` is true, animals nearby target the attacker, too.

`"hunt_non_tamed"` : The animal hunts animals \(food preferences\) when hungry, when not tamed \(in Vanilla\). `"chance"`decides how frequently it hunts\(1 -&gt; 100%, 10 -&gt; 10% ...\). If `"check_sight"` is true, only hunts animals in sight. If `"only_nearby"` is true, only hunts animals nearby. If `"herding"`is true, hunts with other carnivores nearby. This ai\_modifier can not be used with `"hunt"` at the same time.

`"hunt"` : The animal hunts animals \(food preferences\) when hungry, when not tamed \(in Vanilla\). `"chance"` decides how frequently it hunts\(1 -&gt; 100%, 10 -&gt; 10% ...\). If `"check_sight"` is true, only hunts animals in sight. If `"only_nearby"` is true, only hunts animals nearby. If `"herding"` is true, hunts with other carnivores nearby. This ai\_modifier can not be used with `"hunt_non_tamed"` at the same time.

