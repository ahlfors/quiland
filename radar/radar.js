function init() {
    var w = 1500;
    var h = 1200;
    var canvas_color = "#030303";
    var font_color = "white";
    var radar_circle_color = "#36648B";
    var radar_line_color = "#36648B";
    var radar_circle_font_size = 13;
    var radar_quadrant_ctr = 1;
    var quadrantFontSize = 15;
    var headingFontSize = 15;
    var smallLabelFontSize = 13;
    var spacer = 10;
    var fontSize = 12;
    var total_index = 1;

    var lastQuadrant = '';

    $('#title').text(document.title);

//雷达
    var radar = new pv.Panel()
        .width(w)
        .height(h)
        //.strokeStyle(radar_circle_color)
        .fillStyle(canvas_color)
        .canvas('radar');
//大圈
    radar.add(pv.Dot)
        .data(radar_arcs)
        .left(w / 2)
        .bottom(h / 2)
        .radius(function (d) {
            return d.r;
        })
        .strokeStyle(radar_circle_color)
        .anchor("top")
        .add(pv.Label).text(function (d) {
            return d.name;
        })
        .font(radar_circle_font_size + "px Courier New")
        .textStyle(font_color);

//横线
    radar.add(pv.Line)
        .data([(h / 2 - radar_arcs[radar_arcs.length - 1].r), h - (h / 2 - radar_arcs[radar_arcs.length - 1].r)])
        .lineWidth(1)
        .left(w / 2)
        .bottom(function (d) {
            return d;
        })
        .strokeStyle(radar_line_color);

//纵线
    radar.add(pv.Line)
        .data([(w / 2 - radar_arcs[radar_arcs.length - 1].r), w - (w / 2 - radar_arcs[radar_arcs.length - 1].r)])
        .lineWidth(1)
        .bottom(h / 2)
        .left(function (d) {
            return d;
        })
        .strokeStyle(radar_line_color);

    for (var i = 0; i < radar_data.length; i++) {
        //adjust top by the number of headings.
        if (lastQuadrant != radar_data[i].quadrant) {
            radar.add(pv.Label) //大标签
                .left(radar_data[i].left)
                .top(radar_data[i].top)
                .text(radar_data[i].quadrant)
                .strokeStyle(radar_data[i].color)
                .fillStyle(radar_data[i].color)
                .font(quadrantFontSize + "px sans-serif")
                .textStyle("yellow");
            lastQuadrant = radar_data[i].quadrant;
        }

        var itemsByStage = _.groupBy(radar_data[i].items, function (item) {
            return Math.floor(item.pc.r / 100)
        });

        var offsetIndex = 0;
        for (var stageIdx in _(itemsByStage).keys()) {
            if (stageIdx > 0) {
                offsetIndex = offsetIndex + itemsByStage[stageIdx - 1].length + 1;
                console.log("offsetIndex = " + itemsByStage[stageIdx - 1].length, offsetIndex);
            }

            radar.add(pv.Label)//中标签
                .left(radar_data[i].left )
                .top(radar_data[i].top + quadrantFontSize + spacer + (stageIdx * headingFontSize) + (offsetIndex * fontSize))
                .text(radar_arcs[stageIdx].name)
                .font(headingFontSize + "px Courier New")
                .textStyle(font_color);

            radar.add(pv.Label)//小标签
                .left(radar_data[i].left)
                .top(radar_data[i].top + quadrantFontSize + spacer + (stageIdx * headingFontSize) + (offsetIndex * fontSize))
                .strokeStyle(radar_data[i].color)
                .fillStyle(radar_data[i].color)
                .add(pv.Dot)
                .def("i", radar_data[i].top + quadrantFontSize + spacer + (stageIdx * headingFontSize) + spacer + (offsetIndex * fontSize))
                .data(itemsByStage[stageIdx])
                .top(function () {
                    return ( this.i() + (this.index * fontSize) );
                })
                .shape(function (d) {
                    return (d.movement === 't' ? "triangle" : "circle");
                })
                .cursor(function (d) {
                    return ( d.url !== undefined ? "pointer" : "auto" );
                })
                .event("click", function (d) {
                    if (d.url !== undefined) {
                        window.open(d.url, "_blank")
                    }
                })
                .size(fontSize)
                .angle(45)
                .anchor("right")
                .add(pv.Label)
                .text(function (d) {
                    return radar_quadrant_ctr++ + ". " + d.name;
                })
                .font(smallLabelFontSize + "px Monaco")
                .textStyle(font_color);

            radar.add(pv.Dot)//小圈
                .def("active", false)
                .data(itemsByStage[stageIdx])
                .size(function (d) {
                    return ( d.blipSize !== undefined ? d.blipSize : 100 );
                })
                .left(function (d) {
                    var x = polar_to_raster(d.pc.r, d.pc.t)[0];
                    //console.log("name:" + d.name + ", x:" + x);
                    return x;
                })
                .bottom(function (d) {
                    var y = polar_to_raster(d.pc.r, d.pc.t)[1];
                    //console.log("name:" + d.name + ", y:" + y);
                    return y;
                })
                .title(function (d) {
                    return d.name;
                })
                .cursor(function (d) {
                    return ( d.url !== undefined ? "pointer" : "auto" );
                })
                .event("click", function (d) {
                    if (d.url !== undefined) {
                        window.open(d.url, "_blank")
                    }
                })
                .angle(Math.PI)  // 180 degrees in radians !
                .strokeStyle(radar_data[i].color)
                .fillStyle(radar_data[i].color)
                .shape(function (d) {
                    return (d.movement === 't' ? "triangle" : "circle");
                })
                .anchor("center")
                .add(pv.Label)
                .text(function (d) {
                    return total_index++;
                })
                .textBaseline("middle")
                .textStyle(font_color);
        }
    }
    radar.anchor('radar');
    radar.render();
}