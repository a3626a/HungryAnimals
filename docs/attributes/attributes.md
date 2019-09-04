# Attributes

## Introduction

`attributes/{modid}/{animal}.json` registers, or modifies attributes of the animal.

## Grammar

{% code-tabs %}
{% code-tabs-item title="attributes/{modid}/{animal}.json" %}
```text
{
  attribute : attribute_value,
  ...
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="attribute " %}
```text
"hungryanimals.hunger_weight_bmr" |
"hungryanimals.hunger_weight_normal" |
"hungryanimals.hunger_weight_normal_child" |
"hungryanimals.hunger_stomach_digest" |
"hungryanimals.hunger_stomach_max" |
"hungryanimals.courtship_weight" |
"hungryanimals.courtship_probability" |
"hungryanimals.courtship_stomach_condition" |
"hungryanimals.excretion_factor" |
"hungryanimals.child_delay" |
"hungryanimals.child_growing_length" |
"hungryanimals.taming_factor_near_tamed" |
"hungryanimals.taming_factor_near_wild" |
"hungryanimals.taming_factor_food" |
"hungryanimals.fluid_amount" |
"hungryanimals.fluid_weight" |
"generic.maxHealth" |
"generic.movementSpeed" |
"generic.knockbackResistance" |
"generic.armor" |
"generic.armorToughness" |
"generic.followRange" |
"generic.attackDamage" |
"generic.flyingSpeed" |
"generic.attackSpeed" |
"generic.luck"
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="attribute\_value" %}
```text
{
  "value" : double,
  "should_register" : boolean
}
|
double
```
{% endcode-tabs-item %}
{% endcode-tabs %}

## Usage

`"value"` determines base value of the attribute,`"should_register"` determines this attribute should be registered by HA. `"should_register"` must be true, if no other mods \(including Vanilla\) register the attribute,`"should_register"` must be false, if other mods \(including Vanilla\) register the attribute. `"should_register"` can be omitted like :

```text
{
    attribute : double,
    ...
}
```

Then, assigned `double` value is used as `"value"` field, and default `"should_register"` of the `attribute` is used. This default values work very well, but there's a exception. In case of `"generic.attackDamage"`, `"should_register"` should be false for `"minecraft/wolf"`, \(because `"generic.attackDamage"` is registered by Minecraft\)`"should_register"` should be true for `"minecraft/cow"`, Therefore, default value of `"should_register"` for `"minecraft/wolf"` will crash Minecraft.  
  
The default `"should_register"` values are :  
`"hungryanimals.*"` : true \(every attributes added by Hungry Animals\)  
`"generic.maxHealth"` : false  
`"generic.movementSpeed"` : false  
`"generic.knockbackResistance"` : false  
`"generic.armor"` : false  
`"generic.armorToughness"` : false  
`"generic.followRange"` : false  
`"generic.attackDamage"` : true  
`"generic.flyingSpeed"` : true  
`"generic.attackSpeed"` : true  
`"generic.luck"` : true

