# DottedLine
Drawing a round-dotted line with a shape drawable in Android requires you to disable hardware rendering due to some bugs. It also doesn't play nice with wrap_content. Let's not worry about that anymore.

## Usage
You can use _com.xfinity.dahdit.DottedLine_ as a view in XML. By default, the line will be drawn horizontally, with dots of diameter 4dp with minimum 2dp spacing.

## Spacing
The library will calculate the correct amount of spacing to add between dots in order to ensure the dots are flush to the ends of your included View.

## Customizable
The View can have the dot's radius, the minimum gap between dots, the color, and the orientation of the gap customized in XML.

## In Action

Here's what the sample app looks like if you launch it:

![Demo showing line appearance](dahdit-sample.png?raw=true)

Note the ImageView using wrap_content can't be seen at all.
