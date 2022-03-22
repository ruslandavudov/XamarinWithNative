using System;
using Android.App;
using Android.OS;
using Android.Runtime;
using Android.Views;
using AndroidX.AppCompat.Widget;
using AndroidX.AppCompat.App;
using Google.Android.Material.FloatingActionButton;
using Google.Android.Material.Snackbar;
using Google.Android.Material.Button;

using Kotlin.Jvm.Functions;
using Com.Example.Nativelib;
using Android.Widget;

namespace AndroidNativeTest
{

    class Function1Impl<T> : Java.Lang.Object, IFunction1 where T : Java.Lang.Object
    {
        private readonly Action<T> _onInvoked;

        public Function1Impl(Action<T> onInvoked)
        {
            _onInvoked = onInvoked;
        }

        public Java.Lang.Object Invoke(Java.Lang.Object objParameter)
        {
            try
            {
                T parameter = (T)objParameter;
                _onInvoked?.Invoke(parameter);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }

            return null;
        }
    }

    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme.NoActionBar", MainLauncher = true)]
    public class MainActivity : AppCompatActivity
    {
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_main);

            buttonsConfigure();
            initNativeLib();
        }

        private void buttonsConfigure()
        {
            var button = FindViewById<Button>(Resource.Id.bOpen);
            button.Click += ButtonOnClick;
        }

        private void initNativeLib()
        {
            var configuration = new Com.Example.Nativelib.Models.Configuration("", null, null, Java.Lang.Boolean.False, null);

            var nativeLib = NativeLib.Init(configuration);

            nativeLib.OnApply(new Function1Impl<OperationResponse>((response) => {
                changeConfiguration(response.FieldBool, response.FieldString);

                var switcher = FindViewById<Switch>(Resource.Id.fieldBool);
                var textInputEditText = FindViewById<Google.Android.Material.TextField.TextInputEditText>(Resource.Id.fieldString);

                switcher.Checked = (bool)response.FieldBool;
                textInputEditText.Text = response.FieldString;
            }));
        }

        private void changeConfiguration(Java.Lang.Boolean fBool, string fString)
        {
            var configuration = NativeLib.Config;
            

            if (configuration != null)
            {
                configuration.FieldString = fString;
                configuration.FieldBool = fBool;
                NativeLib.SetConfig(configuration);
            }
        }

        private void ButtonOnClick(object sender, EventArgs eventArgs)
        {
            var nativeLib = NativeLib.Instance;

            var switcher = FindViewById<Switch>(Resource.Id.fieldBool);
            var textInputEditText = FindViewById<Google.Android.Material.TextField.TextInputEditText>(Resource.Id.fieldString);


            changeConfiguration((Java.Lang.Boolean)switcher.Checked, textInputEditText.Text);

            nativeLib.Start(this);
        }

        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
