# Fluid

## Introduction

`'food_preferences/fluid/{modid}/{animal}.json'` contains list of fluids which provides `"nutrient"` and `"stomach"`. This fluid preferences decides amount of `"nutrient"` and `"stomach"`provided by AI drink milk .

## Grammar

{% code title="food\_preferences/fluid/{modid}/{animal}.json" %}
```text
[
  {
    "fluid": string,
    "nutrient": double,
    "stomach": double
  },
  ...
]
```
{% endcode %}



