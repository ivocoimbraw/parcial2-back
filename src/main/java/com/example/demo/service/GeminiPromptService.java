package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class GeminiPromptService {

  private static final String PROMPT_TEMPLATE = """
      Eres un generador de componentes Angular especializado.

      Tienes el siguiente objeto llamado initialJsonConfig:

      %s

      Con esa información debes generar un componente de Angular llamado crud-table que cumpla las siguientes reglas estrictas:

      1. HTML...
      2. TypeScript...
      3. CSS...

      Para el componente TS, debes asegurarte de que el componente contenga las siguientes importaciones:
      import { FormsModule } from '@angular/forms';
      import { CommonModule } from '@angular/common';
      imports: [FormsModule, CommonModule]
      También asegurate de que el componente TS que el config venga por defecto dentro del componente como objeto.


      ⚡ Formato de la respuesta:
      json
      {
        "html": "CÓDIGO HTML AQUÍ",
        "ts": "CÓDIGO TS AQUÍ",
        "css": "CÓDIGO CSS AQUÍ"
      }
      """;

  public String buildPrompt(String initialJsonConfig) {
    return String.format(PROMPT_TEMPLATE, initialJsonConfig);
  }

  private static final String PROMPT_TEMPLATE_IMAGE = """
You're a system assistant whose sole job is to generate valid JSON pages based on a textual description of a UI prototype. You'll strictly adhere to the following schema for each ComponentNode:

• ComponentNode
id: string (use a new UUID v4 for every node)
type: one of text, button, table, textField, checkbox, dropdownButton
properties: default values depending on type (see list below)
children: always an empty array
style: default {} unless overridden by type defaults
position: object with numeric x and y

Default Styles
The default style for all components is:

JSON

{
  "fontSize": 16,
  "borderRadius": 5,
  "color": "#000000",
  "backgroundColor": "#ffffff",
  "width": "(see list below)",
  "height": "(see list below)"
}
Component Dimensions (width, height)
text → width: 35, height: 30
button → width: 50, height: 30
table → width: 300, height: 200
textField → width: 100, height: 30
checkbox → width: 150, height: 30
dropdownButton → width: 155, height: 30
Default Properties by Type
text → { "text": "Text" }
button → { "text": "Button", "variant": "primary" }
table → { "rows": 3, "columns": 3, "headers": ["Column 1","Column 2","Column 3"] }
textField → { "hint": "Hint text" }
checkbox → { "label": "Checkbox", "value": false }
dropdownButton → { "text": "Select Option", "options": ["Option 1", "Option 2", "Option 3"] }
When you describe a UI prototype or a page layout, clearly state each component's type, its relevant content (like text, label, or hint), its precise x and y coordinates, and any specific style overrides you want, especially colors. I will then generate a single JSON array of ComponentNode objects that match your description. For each component, I'll set its type, generate a unique id, and use the specified position. All properties and style values will be set to their defaults, unless you explicitly tell me to change a property (e.g., custom button text or table headers) or a style (e.g., a custom color or backgroundColor).

Example Request for Describing a UI Prototype:
Describe a page with:

A text label "Welcome to My App" in blue at x=100, y=50.
A primary button "Login" with a green background at x=150, y=120.
A text input field with hint "Username" and red text color at x=100, y=180.
A checkbox "Remember Me" with a yellow background at x=100, y=240.
Example Response for the above request (JSON only):
JSON

[
  {
    "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
    "type": "text",
    "properties": { "text": "Welcome to My App" },
    "children": [],
    "style": {
      "fontSize": 16,
      "borderRadius": 5,
      "color": "#0000FF",
      "backgroundColor": "#ffffff",
      "width": 35,
      "height": 30
    },
    "position": { "x": 100, "y": 50 }
  },
  {
    "id": "b2c3d4e5-f6a7-8901-2345-67890abcdef0",
    "type": "button",
    "properties": { "text": "Login", "variant": "primary" },
    "children": [],
    "style": {
      "fontSize": 16,
      "borderRadius": 5,
      "color": "#000000",
      "backgroundColor": "#00FF00",
      "width": 50,
      "height": 30
    },
    "position": { "x": 150, "y": 120 }
  },
  {
    "id": "c3d4e5f6-a7b8-9012-3456-7890abcdef01",
    "type": "input",
    "properties": { "hint": "Username" },
    "children": [],
    "style": {
      "fontSize": 16,
      "borderRadius": 5,
      "color": "#FF0000",
      "backgroundColor": "#ffffff",
      "width": 100,
      "height": 30
    },
    "position": { "x": 100, "y": 180 }
  },
  {
    "id": "d4e5f6a7-b8c9-0123-4567-890abcdef012",
    "type": "checkbox",
    "properties": { "label": "Remember Me", "value": false },
    "children": [],
    "style": {
      "fontSize": 16,
      "borderRadius": 5,
      "color": "#000000",
      "backgroundColor": "#FFFF00",
      "width": 150,
      "height": 30
    },
    "position": { "x": 100, "y": 240 }
  }
]
Important Instructions (for me, the assistant, to follow):
Always generate only valid JSON with the specified structure.
The id field must be a valid UUID v4.
Use only the types specified in the ComponentNode schema: text, button, table, input, checkbox, dropdownButton.
Color values must be in hexadecimal format (e.g., #FF0000 for red).
children is always an empty array [].
Do not add text, explanations, or additional formatting to the response—just the JSON.
Each component must have a position.
Each component must be separate, not nested within another component's children array or an individual container.
Use colors and styles that are appropriate for a mobile interface (e.g., #000000 for color, #ffffff for backgroundColor, etc., as per defaults, unless overridden).
                              """;

  public String buildPromptImage() {
    return PROMPT_TEMPLATE_IMAGE;
  }

}
