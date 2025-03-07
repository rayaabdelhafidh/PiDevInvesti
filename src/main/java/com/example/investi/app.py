from flask import Flask, request, jsonify
import pandas as pd
from sklearn.tree import DecisionTreeClassifier
from sklearn.preprocessing import LabelEncoder

# Create Flask app
app = Flask(__name__)

# Load and train the model (use the same training process as before)
model = DecisionTreeClassifier()
encoder = LabelEncoder()

# Sample data for training (replace with your actual training data)
data = {
    'cumulInvest': [140, 100, 140, 140, 140],
    'amount': [114.0, 95.0, 9.5, 9.5, 9.5],
    'totalReturn': [0, 0, 0, 0, 0],
    'amountNeeded': [1500, 2000, 1500, 1500, 1500],
    'investmentProgress': ['NOT_STARTED', 'FAILED', 'IN_PROGRESS', 'COMPLETED', 'NOT_STARTED'],
    'daysSinceStart': [0, 1887, 0, 0, 0]
}

df = pd.DataFrame(data)
y = encoder.fit_transform(df['investmentProgress'])
X = df.drop(columns=['investmentProgress'])

# Train the model
model.fit(X, y)

# Define the prediction endpoint
@app.route('/predict', methods=['POST'])
def predict():
    # Get the JSON data sent in the request
    data = request.get_json(force=True)

    # Convert to DataFrame
    input_data = pd.DataFrame([data])

    # Ensure the input data contains all the necessary columns
    expected_columns = ['cumulInvest', 'amount', 'totalReturn', 'amountNeeded', 'daysSinceStart']

    # Check if any columns are missing and add them with default values (e.g., 0 or NaN)
    for col in expected_columns:
        if col not in input_data.columns:
            input_data[col] = 0  # or you can set to NaN if more appropriate

    # Reorder columns to match the training data
    input_data = input_data[expected_columns]

    # Make predictions
    prediction = model.predict(input_data)

    # Decode the prediction back to the original labels
    prediction_text = encoder.inverse_transform(prediction)

    # Return the prediction as a JSON response
    return jsonify({'prediction': prediction_text[0]})

# Run the Flask app
if __name__ == '__main__':
    app.run(debug=True)
