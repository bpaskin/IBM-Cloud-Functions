<?php 

function main(array $args) : array
{
    $countries = array("Albania",
        "Armenia",
        "Australia",
        "Austria",
        "Azerbaijan",
        "Belgium",
        "Bulgaria",
        "Croatia",
        "Cyprus",
        "CzechRepublic",
        "Denmark",
        "Estonia",
        "Finland",
        "France",
        "Germany",
        "Georgia",
        "Greece",
        "Iceland",
        "Ireland",
        "Israel",
        "Italy",
        "Latvia",
        "Lithuania",
        "Malta",
        "Moldova",
        "Montenegro",
        "Netherlands",
        "NorthMacedonia",
        "Norway",
        "Poland",
        "Portugal",
        "Romania",
        "SanMarino",
        "Serbia",
        "Slovenia",
        "Spain",
        "Sweden",
        "Switzerland",
        "Ukraine",
        "UnitedKingdom");
    
    $selection = "";
    
    foreach ($countries as &$country) {
        $selection = $selection . "<option value='$country'>$country</option>";
    }
    
    $page = "
        <html>
        <head>
            <title>Eurovision Voting 2022</title>
        </head>
        <body>
            <img src=\"https://static.eurovision.tv/dist/assets/images/esc/2022/logo-black.b9b5bfc57b81d725d184..svg\">
            <H1>Make your Eurovision Song Contest Selection</h1>
            <form action=\"voting\" method=\"POST\">
                <select name='selection'>
                $selection
                </selection>
                <input type=\"submit\">
            </form>
        </body>
        </html>
    ";
    
    return ["body" => $page];
}

?>