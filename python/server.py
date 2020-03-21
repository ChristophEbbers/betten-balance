from flask import Flask, render_template, request, redirect, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow
import os
import datetime


# Init app
app = Flask(__name__)
base_dir = os.path.abspath(os.path.dirname(__file__))

# Database
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + os.path.join(base_dir, 'db.sqlite')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

# Init db
db = SQLAlchemy(app)

# Init ma
ma = Marshmallow(app)

# Class for ressources
class example_cl(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    content = db.Column(db.String(200), nullable=False)
    name = db.Column(db.String(100), unique=True)
    date_created = db.Column(db.DateTime, default=datetime.datetime.utcnow())

    def __init__(self, content, name):
        self.name = name
        self.content = content

# Example Schema
class ProductSchema(ma.Schema):
    #fields that should be shown
    class Meta:
        fields = ('id', 'name', 'content', 'date_created')

# Init Schema
product_schema = ProductSchema()
products_schema = ProductSchema(many=True) # for multiple

# Create Entry
@app.route('/add', methods=['POST'])
def add_entries():
    name = request.json['name']
    content = request.json['content']

    new_entry = example_cl(name, content)
    db.session.add(new_entry)
    db.session.commit()
    return product_schema.jsonify(new_entry)


# Update Entry
@app.route('/entry/<id>', methods=['PUT'])
def update_entry(id):
    entry = example_cl.query.get(id)

    name = request.json['name']
    content = request.json['content']

    entry.name = name
    entry.content = content
    db.session.commit()
    return product_schema.jsonify(entry)

# Get all entries
@app.route('/all_entries', methods=['GET'])
def get_all():
    all_entries = example_cl.query.all()
    result = products_schema.dump(all_entries)
    return jsonify(result)

# Get entry
@app.route('/entry/<id>', methods=['GET'])
def get_entry(id):
    entry = example_cl.query.get(id)
    return product_schema.jsonify(entry)


# delete entry
@app.route('/entry/<id>', methods=['delete'])
def delete_entry(id):
    entry = example_cl.query.get(id)
    db.session.delete(entry)
    db.session.commit()
    return product_schema.jsonify(entry)

@app.route('/template', methods=['GET'])
def index():
    if request.method == 'POST':
        try:
            db.session.add('Example')
            db.session.commit()
            return 'updated'
        except:
            return 'exception'

    else:
        return render_template("index.html")


@app.route('/test', methods=['GET'])
def test_msg():
    return jsonify({'msg': 'Test_message'})

@app.route('/delete')
def delete(id):
    task_to_delete = example_cl.query.get_or_404(id)
    db.session.delete(task_to_delete)
    db.session.configure()
    return redirect('/')

# Run Server
if __name__ == "__main__":
    app.run(debug=True)