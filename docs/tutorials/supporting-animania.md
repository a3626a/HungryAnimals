# Supporting Animania

## Introduction

\(...\)

## Registering Animania Animals\(Entities\)

### Animal Registry

For the first, you should register Animania animals. In case of Hampshire, you can registered them like this. For each species, Animania has 3 kinds of entities, male, female, and baby.

{% code-tabs %}
{% code-tabs-item title="animal.json" %}
```text
[
  "minecraft:cow",
  "minecraft:chicken",
  ...
  "animania:hog_hampshire",
  "animania:sow_hampshire",
  "animania:piglet_hampshire"
]
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% hint style="info" %}
For the details about `animal.json`, refer this document. 

{% page-ref page="../basics/animal-registry.md" %}
{% endhint %}

### Attribute Registry

There are a few more steps needed to register Animania animals. Next step is registering attributes. Some HA features requires attributes to work properly. For example HA's weight system needs each species' standard weight, starving weight, baby weight, weight loss per tick, and so on.

Fortunately, you can just copy vanilla pigs' attributes inside `config/hungryanimals_example/attributes/minecraft/pig.json`. 

{% code-tabs %}
{% code-tabs-item title="attributes/minecraft/pig.json" %}
```text
{
  "hungryanimals.hunger_weight_bmr": 0.005981396,
  "hungryanimals.hunger_weight_normal": 100,
  "hungryanimals.hunger_weight_normal_child": 10,
  "hungryanimals.hunger_stomach_digest": 0.029906975,
  "hungryanimals.hunger_stomach_max": 35.88837,
  "hungryanimals.courtship_weight": 0.0,
  "hungryanimals.courtship_probability": 0.05,
  "hungryanimals.courtship_stomach_condition": 0.8,
  "hungryanimals.excretion_factor": 0.025,
  "hungryanimals.child_delay": 22291.25,
  "hungryanimals.child_growing_length": 75233.25,
  "hungryanimals.taming_factor_near_tamed": 0.999,
  "hungryanimals.taming_factor_near_wild": 0.9975,
  "hungryanimals.taming_factor_food": 5.0E-4,
  "generic.maxHealth": 20,
  "generic.movementSpeed": 0.25
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% hint style="info" %}
For the details,

{% page-ref page="../attributes/attributes.md" %}
{% endhint %}

Now, you can copy `pig.json`into `attributes/animania` and rename it like `attributes/animania/hog_hampshire.json`, `attributes/animania/sow_hampshire.json`, and `attributes/animania/piglet_hampshire.json`.

### AI Registry

Copy `config/hungryanimals_example/ais/minecraft/pig.json` to `config/hungryanimals/ais/animania/hog_hampshire.json`, `config/hungryanimals/ais/animania/sow_hampshire.json` and `config/hungryanimals/ais/animania/piglet_hampshire.json`.

{% hint style="info" %}
{% page-ref page="../ais/ais.md" %}
{% endhint %}

### Food Preference Registry

Copy `config/hungryanimals_example/food_preferences/block/minecraft/pig.json` to `config/hungryanimals/food_preferences/block/animania/hog_hampshire.json`, `config/hungryanimals/food_preferences/block/animania/sow_hampshire.json` and `config/hungryanimals/food_preferences/block/animania/piglet_hampshire.json`.

Copy `config/hungryanimals_example/food_preferences/item/minecraft/pig.json` to `config/hungryanimals/food_preferences/item/animania/hog_hampshire.json`, `config/hungryanimals/food_preferences/item/animania/sow_hampshire.json` and `config/hungryanimals/food_preferences/item/animania/piglet_hampshire.json`.

{% hint style="info" %}
{% page-ref page="../food-preferences/introduction.md" %}
{% endhint %}

## Disabling Conflicting HA Features

Unfortunately, Animania has its own food, taming, and breeding system. So I think it is better to use Animania's system by turning off HA's feature. There are still lots of features in HA to combine with Animania.

{% code-tabs %}
{% code-tabs-item title="animal.json" %}
```text
[
  ...
  {
    "name": "animania:hog_hampshire",
    "tamable": false,
    "model_growing": false,
    "sexual": false,
    "hungry": false,
    "ageable": false
  },
  ...
]

```
{% endcode-tabs-item %}
{% endcode-tabs %}

## Useful HA Features

### Productions

HA provides production customization. In this article, we will add milk production to Hampshire.

{% hint style="info" %}
{% page-ref page="../productions/productions.md" %}
{% endhint %}

#### Adding Milk Production

{% code-tabs %}
{% code-tabs-item title="config/hungryanimals/productions/animania/sow\_hampshire.json" %}
```text
[
  {
    "type": "fluid",
    "name": "animania.pig.milk",
    "condition": {
    },
    "fluid": "milk",
    "capacity": 3000,
    "amount": 1,
    "weight": 0
  }
]
```
{% endcode-tabs-item %}
{% endcode-tabs %}

This production entry will make every sow produce milk. Because fluid production requires certain attributes, you should register new attributes to them.

{% code-tabs %}
{% code-tabs-item title="config/hungryanimals/attributes/animania/sow\_hampshire.json" %}
```text
{
  ...
  "hungryanimals.fluid_amount": 4,
  "hungryanimals.fluid_weight": 0.0
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

### Loot Tables

HA provides loot table customization. You can add custom drops to animals. No document for loot tables now.

### AIs

HA provides complicated AI customization. Because most features are disabled, there are just a few useful things.

#### Making Animals Defensive

Refer `config/hungryanimals_example/ais/minecraft/cows.json`.

#### Making Animals Running Away

To enable this, taming system should be enabled. It is because without taming system, every animals are considered as domesticated. Taming systems from HA and Animania are not compatible now. You should tame animals in both method.

{% code-tabs %}
{% code-tabs-item title="config/hungryanimals/animal.json" %}
```text
[
  ...
  {
    "name": "animania:hog_hampshire",
    "tamable": true,
    "model_growing": false,
    "sexual": false,
    "hungry": false,
    "ageable": false
  },
  ...
]
```
{% endcode-tabs-item %}
{% endcode-tabs %}

