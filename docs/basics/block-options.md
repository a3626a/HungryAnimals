# Block Options

## Introduction

`world.json` contains a list of fields related to excreta and niter bed.

## Grammar

{% code title="world.json" %}
```text
{
  "disease_probability" : double,
  "erosion_probability_on_hay" : double,
  "erosion_probability" : double,
  "fermentation_probability" : double,
  "fertilization_probability" : double,
  "ripening_probability" : double
}
```
{% endcode %}

## Usage

`"disease_probability"` : probability\(chance\) to occur disease from excreta.  
`"erosion_probability_on_hay"` : probability\(chance\) to erode excreta.  
`"erosion_probability"` : probability\(chance\) to erode excreta on hay floor cover.  
`"fermentation_probability"` : probability\(chance\) to ferment excreta to manure.  
`"fertilization_probability"` : probability\(chance\) excreta to fertilize block underneath.  
`"ripening_probability"` : probability\(chance\) to ripen niter bed.  
Although all these fields are probability, you can consider these as speed. Higher number at most 1.0 will fasten the process.

