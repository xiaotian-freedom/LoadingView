# LoadingView
可以实现以下功能：

1、整个view的大小。

2、自定义圆圈的颜色、边框的大小、转动的速度。

3、加载成功及失败时的颜色、动画时长。 

4、加载成功和失败可以选择是否回调动画结束接口。 

5、自定义圆圈、成功、失败的动画路径。 

6、重置加载动画。


效果如下：

![image](https://github.com/xiaotianyilang/LoadingView/blob/master/loading_view_success.gif)
![image](https://github.com/xiaotianyilang/LoadingView/blob/master/loading_view_fail.gif)
![image](https://github.com/xiaotianyilang/LoadingView/blob/master/loading_view_reset.gif)

如何使用：
在gradle中添加

compile 'storn.github.io.loadingview:CircleLoadingView:1.1'


在layout中设置如下：

```
<storn.github.io.loadingview.CircleLoadingView

                    android:id="@+id/login_progress"
                   
                    android:layout_width="50dp"
                    
                    android:layout_height="50dp"
                    
                    app:loading_circle_color="@color/colorPrimary"
                    
                    app:loading_duration="1500"
                    
                    app:loading_fail_color="@color/color_r"
                    
                    app:loading_stroke_width="3dp"
                    
                    app:loading_style="stroke_loading"
                    
                    app:loading_success_color="@color/colorPrimary" />
```
           
配置说明：

```
loading_circle_color：自定义加载条的颜色

loading_duration：自定义转动一周的时间

loading_success_color：加载成功时显示的颜色

loading_fail_color：加载失败时显示的颜色

loading_stroke_width：加载条的宽度

loading_style：加载条的样式，默认为stroke。
```

代码中设置：
```
加载成功时调用mProgressView.success();

加载失败时调用mProgressView.fail();
```
