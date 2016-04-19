# MaterialIntroView [Beta]

This Library is fork from [iammert/MaterialIntroView](https://github.com/iammert/MaterialIntroView) ,I add a feature which can surpport custom info view

#Screen
<img src="https://raw.githubusercontent.com/iammert/MaterialIntroView/master/art/materialintroviewgif.gif"/>

#Usage
```java
new MaterialIntroView.Builder(this)
                .enableDotAnimation(true)
				.enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText("Hi There! Click this card and see what happens.")
                .setShapeType(ShapeType.CIRCLE)
                .setTarget(view)
                .setUsageId("intro_card") //THIS SHOULD BE UNIQUE ID
                .show();
```

# Import

Module build.gradle
```java
dependencies {
  compile 'com.yamlee:materialintro:1.0.2'
}
```

# Builder Methods
```java
.setMaskColor(Color.Blue)
```

```java
.setDelayMillis(3000) //starts after 3 seconds passed
```

```java
.enableFadeAnimation(true) //View will appear/disappear with fade in/out animation
```

```java
//ie. If your button's width has MATCH_PARENT.
//Focus.ALL is not a good option. You can use
//Focus.MINIMUM or Focus.NORMAL. See demos below.
.setFocusType(Focus.MINIMUM)
.setFocusType(Focus.NORMAL)
.setFocusType(Focus.ALL)
```

```java
//ie. You can focus on left of RecyclerView list item.
.setFocusGravity(FocusGravity.LEFT)
.setFocusType(FocusGravity.CENTER)
.setFocusType(FocusGravity.RIGHT)
```

```java
.setTarget(myButton) //Focus on myButton
```

```java
.setTargetPadding(30) //add 30px padding to focus circle
```

```java
.setInfoText("This is info text!") //Setting text will enable info dialog
```

```java
.setTextColor(Color.Black) //Info dialog's text color is set to black
```

```java
.setInfoTextSize(30) //Change text size
```

```java
.setShapeType(ShapeType.CIRCLE) //Change shape of focus area
.setShapeType(ShapeType.RECTANGLE) //Change shape of focus area
```

```java
.setCustomShape(Shape shape) //Use custom shape
```

```java
// Allow this showcase overlay to only show up once. Prevents multiple screens from showing at the same time.
// Useful if you wish to show a tour step in a code that gets called multiple times
.setIdempotent(true)
```

```java
.setUsageId("intro_fab_button") //Store intro view status whether it is learnt or not
```

```java
.enableDotAnimation(true) //Shows dot animation center of focus area
```

```java
.enableIcon(false) //Turn off helper icon, default is true
```

```java
.performClick(true) //Trigger click operation when user click focused area.
```

```java
//If you don't want to perform click automatically
//You can disable perform clik and handle it yourself
.setListener(new MaterialIntroListener() {
                    @Override
                    public void onUserClicked(String materialIntroViewId) {
                      //to do click
                    }
                })
```

```java
//If you want add custom info view ,you can set a InfoViewConfiguration
.setInfoViewConfiguration(new InfoViewConfiguration())
``

# Configuration Method
```java
//Create global config instance to not write same config to builder
//again and again.
MaterialIntroConfiguration config = new MaterialIntroConfiguration();
config.setDelayMillis(1000);
config.setFadeAnimationEnabled(true);
...
.setConfiguration(config) //
```

# Use Custom Shapes
You can use your own highlight shapes if Circle and Rectangle do not work for you. See source for `Circle` and `Rect` for implementation example.

```java
public class MyShape extends Shape {
    // ... your implementation
}
//... in your app code
.setCustomShape(MyShape shape)
```

# Use Custom Info View

```java
  private MaterialIntroView buildIntro(Context context, View target) {
        LayoutInflater inflater = LayoutInflater.from(context);
        MaterialIntroView materialIntroView = (MaterialIntroView) inflater.inflate(R.layout.layout_custom_material_intro_view, null);
        //if you need custom materialIntroView,such as add a "confirm" button ,you can pass a inflate materialIntroView to it's builder constructor
        MaterialIntroView.Builder builder = new MaterialIntroView.Builder(materialIntroView);
        InfoViewConfiguration infoViewConfiguration = buildInfoViewConfiguration(inflater);
        return builder.enableDotAnimation(false)
                .enableIcon(false)
                .setShape(ShapeType.RECTANGLE)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(true)
                .setIdempotent(true)
                .setListener(new MaterialIntroListener() {
                    @Override
                    public void onUserClicked(String materialIntroViewId) {
                        mSecondMaterialIntroView.show();
                    }
                })
                .setInfoText("Hi There! Click this card and see what happens.")
                .setInfoViewConfiguration(infoViewConfiguration)
                .setTarget(target)
                .setUsageId("intro_card") //THIS SHOULD BE UNIQUE ID
                .build();
    }
```

```java
 @NonNull
    private InfoViewConfiguration buildInfoViewConfiguration(LayoutInflater inflater) {
        View customInfoView = inflater.inflate(R.layout.layout_custom_info_arrow_up, null);
        InfoViewConfiguration infoViewConfiguration = new InfoViewConfiguration();
        infoViewConfiguration.setInfoView(customInfoView);
        //Add animation to custom info view
        View ivArrow = customInfoView.findViewById(R.id.iv_arrow);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivArrow, View.TRANSLATION_Y, 0, 20, 0);
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        infoViewConfiguration.setAnimator(objectAnimator);
        infoViewConfiguration.setAlignCenter(true);
        return infoViewConfiguration;
    }
```
> Your custom layout file's viewGroup need be a RelativeLayout,or will throw a Error

# Demos
![Alt text](/art/art_drawer.png?raw=true)
![Alt text](/art/art_focus_all.png?raw=true)
![Alt text](/art/art_focus_normal.png?raw=true)
![Alt text](/art/art_gravity_left.png?raw=true)
![Alt text](/art/art_rectangle.png?raw=true)
# TODO

* [ ] Sample app will be more detailed about using library.
* [ ] Sequence for MaterialIntroViews

#Authors

[Mert SIMSEK](https://github.com/iammert)

[Murat Can BUR](https://github.com/muratcanbur)


License
--------


    Copyright 2015 Mert Şimşek.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


