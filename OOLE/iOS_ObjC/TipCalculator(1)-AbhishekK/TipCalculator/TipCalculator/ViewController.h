//
//  ViewController.h
//  TipCalculator
//
//  Created by Michaelangelo on 4/20/13.
//  Copyright (c) 2013 OOLE. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ViewController : UIViewController
- (IBAction)calculateTotalBill:(id)sender;
@property (strong, nonatomic) IBOutlet UITextField *totalAmount;
- (IBAction)standardTipApplied:(id)sender;
@property (strong, nonatomic) IBOutlet UITextField *tipAmount;
- (IBAction)closeAppln:(id)sender;
@property (strong, nonatomic) IBOutlet UITextField *tipPercent;
- (IBAction)stepperValChange:(id)sender;
@property (strong, nonatomic) IBOutlet UIStepper *tipStepper;
@property (strong, nonatomic) IBOutlet UITextField *originalAmt;
- (IBAction)textFieldReturn:(id)sender;
- (IBAction)backgrndTouched:(id)sender;
- (IBAction)validateFloatVal:(id)sender;
- (IBAction)validateTipVal:(id)sender;
- (IBAction)onBillAmountChange: (id)sender;
- (IBAction)tipAmountChange: (id)sender;
- (double) calculateTip;
- (int) calculateTipPercent;

@end
