import random

# sample output:
#	font-family: "Arial, Courier"; 
#	font-size: 30%; 
#	font-weight: bold; 
#	font-style: italic; 
#	font: bold 24px Arial,Courier; 


normal_fonts = ['Agency FB', 'Algerian', 'Arial', 'Arial Rounded MT', 'Bahnschrift', 'Baskerville Old Face', 'Bauhaus 93', 'Bell MT', 'Berlin Sans FB', 'Bernard MT', 'Blackadder ITC',
                'Bedoni MT', 'Book Antiqua', 'Bookman Old Style', 'Bradley Hand ITC', 'Britannic', 'Broadway', 'Bush Script MT', 'Calibri', 'Californian FB', 'Calisto MT', 'Cambria',
                'Cambria Math', 'Candara', 'Castellar', 'Centaur', 'Century', 'Century Gothic', 'Century Schoolbook', 'Chiller', 'Colonna MT', 'Comic Sans MS', 'Consolas', 'Constantia',
                'Cooper', 'Copperplate Gothic', 'Corbel', 'Courier New', 'Curlz MT', 'Dubai', 'Edwardian Script ITC', 'Elephant', 'Engravers MT', 'Eras ITC', 'Felix Titling',
                'Footlight MT', 'Forte', 'Franklin Gothic', 'Franklin Gothic Book', 'Freestyle Script', 'French Script MT', 'Gabriola', 'Gadugi', 'Garamond', 'Georgia', 'Gigi',
                'Gill Sans', 'Gill Sans MT', 'Gloucester MT', 'Goudy Old Style', 'Goudy Stout', 'Haettenschweiler', 'Harlow Solid', 'Harrington', 'High Tower Text', 'Impact',
                'Imprint MT Shadow', 'Informal Roman', 'Ink Free', 'Jokerman', 'Juice ITC', 'Kristen ITC', 'Kunstler Script', 'Leelawadee', 'Lucida Bright', 'Lucida Calligraphy',
                'Lucida Console', 'Lucida Fax', 'Lucida Handwriting', 'Lucida Sans', 'Lucida Sans Typewriter', 'Lucida Sans Unicode', 'Magneto', 'Maiandra GD',
                'Matura MT Script Capitals', 'Microsoft New Tai Lue', 'Microsoft Sans Serif', 'Microsoft Uighur', 'Mistral', 'Modern No. 20', 'Mongolian Baiti', 'Monotype Corsiva',
                'MS Reference Sans Serif', 'Niagara Engraved', 'Niagara Solid', 'OCR A', 'Old English Text MT', 'Onyx', 'Palace Script MT', 'Palatino Linotype', 'Papyrus', 'Parchment',
                'Perpetua', 'Perpetua Titling MT', 'Playbill', 'Poor Richard', 'Pristina', 'Rage', 'Ravie', 'Rockwell', 'Script MT', 'Segeo Print', 'Segoe Script', 'Segoe UI',
                'Showcard Gothic', 'Sitka', 'Snap ITC', 'Stencil', 'Sylfaen', 'Tahoma', 'Tempus Sans ITC', 'Times New Roman', 'Trebuchet MS', 'Tw Cen MT', 'Verdana', 'Viner Hand ITC',
                'Vivaldi', 'Vladmir Script', 'Wide Latin']

exotic_fonts = ['Bookshelf Symbol 7', 'Ebrima', 'HoloLens MDL2 Assets', 'Javanese Text', 'Leelawadee UI', 'Malgun Gothic', 'Marlett', 'PMingLiU-ExtB', 'Microsoft Himalaya',
                'Microsoft JhengHei', 'Microsoft Jenghei UI', 'Microsoft PhagsPa', 'Microsoft Tai Le', 'Microsoft YaHei', 'Microsoft Yahei UI', 'Microsoft Yi Baiti', 'MingLiU_HKSCS-ExtB',
                'MingLiU-ExtB', 'MS Gothic', 'MS Outlook', 'MS PGothic', 'Segoe MDL2 Assets', 'MS Reference Specialty', 'MS UI Gothic', 'MT Extra', 'Myanmar Text', 'MV Boli',
                'Nirmala UI', 'NSimSun', 'Segoe UI Emoji', 'Segoe UI Historic', 'Segoe UI Symbol', 'SimSun', 'SimSun-ExtB', 'Symbol', 'Webdings', 'Wingdings', 'Wingdings 2',
                'Wingdings 3', 'Yu Gothic', 'Yu Gothic UI']

roll = random.randint(1, 100)
if (roll <= 11):
    font_family = exotic_fonts[random.randint(0, len(exotic_fonts) - 1)]
else:
    font_family = normal_fonts[random.randint(0, len(normal_fonts) - 1)]

roll = random.randint(1, 100)
if (roll <= 11):
    second_font_family = exotic_fonts[random.randint(0, len(exotic_fonts) - 1)]
else:
    second_font_family = normal_fonts[random.randint(0, len(normal_fonts) - 1)]


font_size = random.randint(12, 48)      #use pt for units

roll = random.randint(1,100)
if (roll <= 80):
    font_weight = 'normal'
else:
    roll = random.randint(1,100)
    if (roll <= 17):
        font_weight = 'bold'
    else:
        font_weight = random.randint(1, 9) * 100

roll = random.randint(1, 100)
if (roll <= 7):
    font_style = 'italic'
elif (roll <= 11):
    font_style = 'oblique'
else:
    font_style = 'normal'

print("\tfont-family: ", '"', font_family, '", "', second_font_family, '";', sep='')
print("\tfont-size: ", font_size, 'pt;', sep='')
print("\tfont-weight: ", font_weight, ';', sep='')
print("\tfont-style: ", font_style, ';', sep='')


#sample output
#       text-align: center; 
#	text-align-last: right; 
#	text-indent: 20px; 
#	letter-spacing: 20px; 
#	word-spacing: 20px; 
#	line-height: 20px; 
#	vertical-align: baseline;
#	text-decoration: underline;

roll = random.randint(1, 129)
if (roll <= 2):
    text_align = 'right';
elif (roll <= 55):
    text_align = 'center';
elif (roll <= 100):
    text_align = 'left';
else:
    text_align = 'justify';

roll = random.randint(1, 100)
if (roll <= 71):
    text_align_last = 'left';
elif (roll <= 92):
    text_align_last = 'center';
elif (roll <= 94):
    text_align_last = 'right';
else:
    text_align_last = 'justify';

text_indent = random.randint(3, 6)  #use %

roll = random.randint(1,100)                    #this is just a suggestion, really - you'll have to decide whether to apply these I suspect. 
if (roll <= 11):                                #normally it would depend on the element / where in the page, whether you apply it
    letter_spacing = random.randint(1, 17)      #use pt
    word_spacing = random.randint(letter_spacing, letter_spacing + 42)
else:                                           #you could increase the chance of spacing (21%?) if you figured out where to apply it, apparently 
    letter_spacing = 0
    roll = random.randint(1, 100)
    if (roll <= 4):
        word_spacing = random.randint(1, 42)
    else:
        word_spacing = 0

roll = random.randint(1, 100)
if (roll <= 36):
    line_height = random.randint(1, 28) + font_size     #use pt
else:
    line_height = font_size

matrix = ['baseline', 'sub', 'super', 'text-top', 'text-bottom', 'middle', 'top', 'bottom']
vertical_align = matrix[random.randint(0, len(matrix) - 1)]

matrix = ['underline', 'overline', 'line-through', 'none']
text_decoration = matrix[random.randint(0, len(matrix) - 1)]

print("\ttext-align: ", text_align, ';', sep='')
print("\ttext-align-last: ", text_align_last, ';', sep='')
print("\ttext-indent: ", text_indent, "%;", sep='')
print("\tletter-spacing: ", letter_spacing, "pt;", sep='')
print("\tword-spacing: ", word_spacing, "pt;", sep='')
print("\tline-height: ", line_height, "pt;", sep='')
print("\tvertical-align: ", vertical_align, ';', sep='')
print("\ttext-decoration: ", text_decoration, ';', sep='')

#sample output:
#    color: hsl(0, 25%, 57%)		<!-- hsl(hue, saturation, lightness), hue range is 0-360, saturation & lightness are %.
#OR  color: rgb(0, 255, 143)		<!-- rgb(red, green, blue), ranges are 0-255 -->
roll = random.randint(1, 100)
if (roll <= 91):
    lightness = random.randint(1, 100)
    saturation = random.randint(1, 100)
    hue = random.randint(1, 360)
    print("\tcolor: hsl(", hue, ", ", saturation, "%, ", lightness, "%);", sep='')
else:
    red = random.randint(0, 255)
    green = random.randint(0, 255)
    blue = random.randint(0, 255)
    print("\tcolor: rgb(", red, ", ", green, ", ", blue, ");", sep='')


#sample output:
#    background-color: hsla(0, 25%, 57%)
#OR  color: rgba(0, 255, 143)

roll = random.randint(1, 100)
if (roll <= 84):
    lightness = random.randint(9, 100)
    saturation = random.randint(30, 100)
    hue = random.randint(1, 360)
    roll = random.randint(1, 100)
    if (roll <= 85):
        opacity = 1
    else:
        opacity = random.randint(1, 3)
    print("\tbackground-color: hsla(", hue, ", ", saturation, "%, ", lightness, "%, 0.", opacity, ");", sep='')

#sample output:
#	border-width: thin, 4px, .1em, thick;	<!-- sets width of the border. top, right, bottom, & left in that order; accepts up to 4 values. 
#							<!-- units can be: px, em, rem, pt, thin, medium, thick. % does not work -->
#	border-style: solid dashed;		<!-- top, right, bottom, & left in that order; can accept up to 4 values. -->
#							<!-- values include: none, hidden, dotted, dashed, solid, double, groove, ridge, inset, outset. default is none -->
#	border-color: #CCCC00 #FFFFFF;		<!-- sets colors for each border edge: top, right, bottom, & left in that order; can accept up to 4 values. -->
#	border: 5px dashed #CCC999;		<!-- declare multiple border properties at once -->
#	border-radius: 20px / 10px 30px;










    
