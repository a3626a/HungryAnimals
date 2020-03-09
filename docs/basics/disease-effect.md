# Disease Effect

## Introduction

`disease.json` contains list of fields that affects effect of disease.

## Grammar

{% code title="disease.json" %}
```text
{
  "multiply_weight_bmr" : double,
  "multiply_movement_speed" : double
}
```
{% endcode %}

## Usage

`"multiply_weigth_bmr"` : multiply to weigth bmr of patient  
`"multiply_movement_speed"` : multiply to movement speed of patient  
  
For example, `"multiply_weigth_bmr" : 3.0` will cause the animal to consume 300% more energy\(weight\), as a result overall 400% usage.

