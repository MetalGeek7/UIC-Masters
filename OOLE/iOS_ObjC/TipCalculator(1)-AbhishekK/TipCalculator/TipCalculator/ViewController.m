//
//  ViewController.m
//  TipCalculator
//
//  Created by Michaelangelo on 4/20/13.
//  Copyright (c) 2013 OOLE. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    self.tipStepper.minimumValue = 0;
    self.tipStepper.maximumValue = 100;
    self.tipStepper.stepValue = 1;
    self.tipStepper.wraps = NO;        //default value is NO
    self.tipStepper.autorepeat = YES;  //default value is YES
    self.tipStepper.continuous = YES;  //default value is YES

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)stepperValChange:(id)sender {
    // Logic for defining actions after stepper value is changed during user interaction
   
    double stepperValue = self.tipStepper.value;
    self.tipPercent.text = [NSString stringWithFormat:@"%.f", stepperValue];
    double res = self.calculateTip;
}

- (IBAction)textFieldReturn:(id)sender {
    [sender resignFirstResponder];
}

- (IBAction)backgrndTouched:(id)sender {
    //code for exiting from keyboard input when touched on canvas area
    [self.originalAmt resignFirstResponder];
    [self.tipAmount resignFirstResponder];
}

- (IBAction)validateFloatVal:(id)sender {
    //checks and converts user input to proper floating point number
    double x = [_originalAmt.text doubleValue];
    NSString *reqdVal = [NSString stringWithFormat:@"%.2f", x];
    //NSLog(reqdVal);
    _originalAmt.text = reqdVal;
}

- (IBAction)validateTipVal:(id)sender {
    //checks and converts tip amount to proper floating point number
    double x = [_tipAmount.text doubleValue];
    NSString *reqdVal = [NSString stringWithFormat:@"%.2f", x];
    _tipAmount.text = reqdVal;
}

- (double) calculateTip {
    //Calculates tip amount in dollars from orig amt and tip percent
    double tipVal = 0.0;
    double originalVal = [_originalAmt.text doubleValue];
    if (originalVal !=0) {
        tipVal = originalVal * ([_tipPercent.text floatValue]/100);
        _tipAmount.text = [NSString stringWithFormat:@"%.2f", tipVal];
    }
    else {
        UIAlertView* alert;
        alert = [[UIAlertView alloc] initWithTitle:@"Info" message:@"Bill Amount is Nil" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
        [alert show];
        //[alert release];
        _tipAmount.text = [NSString stringWithFormat:@"%.2f", tipVal];
    }

    return tipVal;
}

- (IBAction)onBillAmountChange:(id)sender{
    double res = self.calculateTip;
}

- (int) calculateTipPercent {
    //Calculates percent of tip from original amt and tip value
    int tipPercent = 0;
    double tipVal = [_tipAmount.text doubleValue];
    
    tipPercent = (tipVal * 100) / ([_originalAmt.text floatValue]);
    _tipPercent.text = [NSString stringWithFormat:@"%d", tipPercent];
    _tipStepper.value = tipPercent;
    return tipPercent;
}

- (IBAction)tipAmountChange:(id)sender {
    
    double originalVal = [_originalAmt.text doubleValue];
    if (originalVal !=0) {
    int tipPercent = self.calculateTipPercent;
        
    }
    else {
        UIAlertView* alert;
        alert = [[UIAlertView alloc] initWithTitle:@"Info" message:@"Bill Amount is Nil" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
        [alert show];
    }
}

- (IBAction)standardTipApplied:(id)sender {
    
    _tipPercent.text = [NSString stringWithFormat:@"%d", 15];
    _tipStepper.value = 15;
    double res = self.calculateTip;
}

- (IBAction)calculateTotalBill:(id)sender {
    
    float result = [_originalAmt.text floatValue] + [_tipAmount.text floatValue];
    _totalAmount.text = [NSString stringWithFormat:@"%.2f", result];
}

- (IBAction)closeAppln:(id)sender {
    exit(0);
}

@end
